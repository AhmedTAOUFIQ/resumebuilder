package com.keiken.pptTemplateGenerator.Handler;

import com.keiken.config.AppProperties;
import com.keiken.mapper.TemplateBaseMapper;
import com.keiken.openai.service.SummaryService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.sl.usermodel.PictureData;
import org.apache.poi.xslf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PPTemplateHandlerImpl implements PPTemplateHandler {

    private final AppProperties appProperties;

    private final SummaryService summaryService;

    @Override
    public XMLSlideShow loadTemplate(String templateFilename) throws IOException {
        ClassPathResource resource = new ClassPathResource(
                appProperties.getTemplates().getPptTemplatesClasspath() + templateFilename
        );
        InputStream inputStream = resource.getInputStream();
        return new XMLSlideShow(inputStream);
    }
    @Override
    public byte[] saveAndCloseTemplate(XMLSlideShow ppt) throws IOException {
        String filename = UUID.randomUUID().toString();
        Path outputFilePath = Paths.get(appProperties.getTemplates().getPptOutputPath(), filename + ".pptx");

        if (Files.notExists(outputFilePath.getParent())) {
            Files.createDirectories(outputFilePath.getParent());
        }

        try (FileOutputStream outputStream = new FileOutputStream(outputFilePath.toFile())) {
            ppt.write(outputStream);
            ppt.close();
        }

        return Files.readAllBytes(outputFilePath);
    }

    @Override
    public String getAltText(XSLFShape shape) {
        String altText = "";
        try {
            XmlObject[] altTextObjects = shape.getXmlObject().selectPath(
                    "declare namespace p='http://schemas.openxmlformats.org/presentationml/2006/main' .//p:cNvPr/@descr"
            );
            if (altTextObjects.length > 0) {
                XmlCursor cursor = altTextObjects[0].newCursor();
                altText = cursor.getTextValue();
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return altText;
    }

    @Override
    public void removeShapeByAltText(XSLFSlide slide, String altText) {
        List<XSLFShape> shapesToRemove = new ArrayList<>();

        for (XSLFShape shape : slide.getShapes()) {
            if (altText.equals(this.getAltText(shape))) {
                shapesToRemove.add(shape);
            }
        }

        for (XSLFShape shape : shapesToRemove) {
            slide.removeShape(shape);
        }
    }

    @Override
    public void replacePictureShape(XSLFPictureShape oldPictureShape, String newPicturePath, String fallbackImagePath)  {
        try {
            // Load the new image from the resources folder
            Resource resource = new ClassPathResource(newPicturePath);

            if(!resource.exists()) {
                resource = new ClassPathResource(fallbackImagePath);
            }

            InputStream imageStream = resource.getInputStream();
            byte[] imageBytes = imageStream.readAllBytes();
            imageStream.close();

            // Add the new image to the presentation
            XMLSlideShow ppt = oldPictureShape.getSheet().getSlideShow();
            XSLFPictureData newPictureData = ppt.addPicture(imageBytes, PictureData.PictureType.PNG);

            // Get the position and size of the old picture shape
            double x = oldPictureShape.getAnchor().getX();
            double y = oldPictureShape.getAnchor().getY();
            double width = oldPictureShape.getAnchor().getWidth();
            double height = oldPictureShape.getAnchor().getHeight();

            // Get the slide
            XSLFSlide slide = (XSLFSlide) oldPictureShape.getSheet();

            // Remove the old picture shape
            slide.removeShape(oldPictureShape);

            // Create a new picture shape with the new picture data
            XSLFPictureShape newPictureShape = slide.createPicture(newPictureData);
            newPictureShape.setAnchor(new java.awt.geom.Rectangle2D.Double(x, y, width, height));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <Obj> void fillTextPlaceholders(XSLFTextParagraph textParagraph, Obj dataObject, int textMaxWords) {
        // Retrieve the text from the shape and replace placeholders
        for (XSLFTextRun textRun : textParagraph.getTextRuns()) {
            String text = textRun.getRawText();

            // Get fields of the class and its super class
            List<Field> fields = new ArrayList<>(Arrays.stream(dataObject.getClass().getDeclaredFields()).toList());
            Class<?> superClass = dataObject.getClass().getSuperclass();
            if(superClass != null) {
                List<Field> superFields = Arrays.stream(superClass.getDeclaredFields()).toList();
                fields.addAll(superFields);
            }

            for(Field field : fields) {
                try {
                    field.setAccessible(true);

                    Object value = field.get(dataObject);

                    if(value instanceof List<?> list && !list.isEmpty()) {
                            String skills = list.stream()
                                    .map(Object::toString)
                                    .collect(Collectors.joining(", "));

                            text = text.replaceAll("\\b" + field.getName() + "\\b", skills);
                    } else {
                        String replacement = value != null ? value.toString() : "";
                        if (replacement.split(" ").length > textMaxWords) {
                            replacement = summaryService.getSummary(replacement, textMaxWords);
                        }
                        text = text.replaceAll("\\b" + field.getName() + "\\b", replacement);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            textRun.setText(text);
        }
    }

    @Override
    public void fillImages(
            List<XSLFPictureShape> shapesToReplace, String[] imagesList, String folderPath, String fallbackImage
    ) {
        // Get Slide from shapes
        XSLFSlide slide = shapesToReplace.get(0).getSheet().getSlideShow().getSlides().get(0);

        // Replace collected shapes
        int index = 0;
        for (XSLFPictureShape pictureShape : shapesToReplace) {
            // Remove unnecessary placeholders
            if(index >= imagesList.length) {
                String altText =getAltText(pictureShape);
                removeShapeByAltText(slide, altText);
                continue;
            }
           replacePictureShape(
                    pictureShape,
                    folderPath + imagesList[index++].toLowerCase() + ".png",
                    folderPath + fallbackImage
            );
        }
    }

    @Override
    public void fillTable(XSLFTable table, List<TemplateBaseMapper.Fact> facts, int textMaxWords)  {
        int factIndex = 0;
        for (XSLFTableRow tableRow : table.getRows()) {
            for (XSLFTableCell tableCell : tableRow.getCells()) {
                if(factIndex > facts.size()){
                    break;
                }
                TemplateBaseMapper.Fact fact = facts.get(factIndex);

                for (XSLFTextParagraph textParagraph : tableCell.getTextParagraphs()) {
                    fillTextPlaceholders(textParagraph, fact, textMaxWords);
                }
                factIndex++;
            }
        }
    }


    @Override
    public void drawCircleFragment(XSLFSlide slide, Point2D center, double radius, double thicknessMM, double angle, Color color) {
        // Convert thickness from mm to points (1 mm = 3.6 points)
        double thicknessPoints = thicknessMM * 3.6;

        // Calculate circle fragment points
        Path2D path = calculateCircleFragmentPath(center, radius, thicknessPoints, angle);

        // Create the circle shape
        XSLFFreeformShape shape = slide.createFreeform();
        shape.setPath(path);
        // Border
        shape.setLineColor(Color.RED);
        shape.setLineWidth(0);

        shape.setFillColor(color);
    }


    @Override
    public Path2D calculateCircleFragmentPath(Point2D center, double radius, double thicknessPoints, double angle) {
        Path2D path = new Path2D.Double();

        double startAngle = -90;

        // Calcul des points pour l'arc extérieur
        for (double i = 0; i <= angle; i++) {
            double radians = Math.toRadians(startAngle + i);
            double x = center.getX() + radius * Math.cos(radians);
            double y = center.getY() + radius * Math.sin(radians);
            if (i == 0) {
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }
        }

        // Calcul des points pour l'arc intérieur
        for (double i = angle; i >= 0; i--) {
            double radians = Math.toRadians(startAngle + i);
            double x = center.getX() + (radius - thicknessPoints) * Math.cos(radians);
            double y = center.getY() + (radius - thicknessPoints) * Math.sin(radians);
            path.lineTo(x, y);
        }

        // Fermer le chemin pour former le fragment
        path.closePath();

        return path;
    }
}




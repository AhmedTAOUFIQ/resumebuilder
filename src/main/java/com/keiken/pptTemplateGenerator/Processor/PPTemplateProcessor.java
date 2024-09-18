package com.keiken.pptTemplateGenerator.Processor;

import com.keiken.config.AppProperties;
import com.keiken.pptTemplateGenerator.Handler.PPTemplateHandler;
import com.keiken.mapper.PlaceholderImageMapping;
import com.keiken.mapper.TemplateBaseMapper;
import com.keiken.processor.TemplateProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.apache.poi.xslf.usermodel.*;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Component("pptx")
@RequiredArgsConstructor
@Slf4j
public class PPTemplateProcessor implements TemplateProcessor {

    private final PPTemplateHandler templateHandler;

    private final AppProperties appProperties;


    @Override
    public byte[] processTemplate(String templateFilename, TemplateBaseMapper data) {
        try {
            // --> Load Template
            XMLSlideShow ppt = templateHandler.loadTemplate(templateFilename);


            Map<String, XSLFPictureShape> pictureShapesToReplace = new HashMap<>();
            // Find and manipulate shapes
            for (XSLFShape shape : ppt.getSlides().get(0).getShapes()) {
                if (shape instanceof XSLFTextBox textShape) {
                    processTextBoxShapes(textShape, data);
                }
                else if (shape instanceof XSLFTable table) {
                    processTable(table, data);
                }
                else if (shape instanceof XSLFPictureShape pictureShape) {
                    collectPictureShapes(pictureShape, data, pictureShapesToReplace);
                }
            }
            // Process and Replace Collected picture shapes
            processPictureShapes(pictureShapesToReplace, data);


            log.info("PPT Template Process Completed");
            // --> Save and Close Template
            return templateHandler.saveAndCloseTemplate(ppt);

        } catch (IOException e) {
            e.printStackTrace();
        }

        log.info("PPT Template Process Failed");
        return new byte[0];
    }

    private void processTextBoxShapes(XSLFTextBox textShape, TemplateBaseMapper data) {
        for(XSLFTextParagraph textParagraph : textShape.getTextParagraphs()) {
            templateHandler.fillTextPlaceholders(textParagraph , data, 20);
        }
    }

    private void processTable(XSLFTable table, TemplateBaseMapper data) {
        templateHandler.fillTable(table, data.getFacts(), 20);
    }

    private void collectPictureShapes(
            XSLFPictureShape pictureShape, TemplateBaseMapper data, Map<String, XSLFPictureShape> shapesToReplace
    ) {
        String altText = templateHandler.getAltText(pictureShape);
        for(PlaceholderImageMapping mapping : data.getPlaceholderImageMappings()) {
            for(int i = 0; i<mapping.getNumberOfImagesToReplace(); i++) {
                String placeholder = String.format(mapping.getPlaceholdersPattern(), i);
                if(altText.equals(placeholder)) {
                    shapesToReplace.put(placeholder, pictureShape);
                    return;
                }
            }
        }
    }

    private void processPictureShapes(Map<String, XSLFPictureShape> shapesToReplace, TemplateBaseMapper data) {
        String imagesPath = appProperties.getTemplates().getImagesClasspath();
        try {
            for(PlaceholderImageMapping mapping : data.getPlaceholderImageMappings()) {
                String[] imagesList = (String[]) MethodUtils.invokeMethod(data, mapping.getDataSourceMethod());

                for(int i = 0; i < mapping.getNumberOfImagesToReplace(); i++) {
                    String placeholder = String.format(mapping.getPlaceholdersPattern(), i);
                    XSLFPictureShape shape = shapesToReplace.get(placeholder);
                    XSLFSlide slide = shape.getSheet().getSlideShow().getSlides().get(0);

                    if(i < imagesList.length) {
                        // Need to format image name to respect images folder names pattern
                        String newPicturePath = imagesPath + mapping.getImageFolderPath() + imagesList[i] + ".png";
                        String fallbackImagePath = imagesPath + mapping.getImageFolderPath() + mapping.getFallbackImageName() + ".png";

                        if(mapping.applyProgressAroundImage() != null) {
                            Integer[] progressList = (Integer[]) MethodUtils.invokeMethod(data, mapping.getProgressPercentagesMethod());
                            Rectangle2D anchor = shape.getAnchor();
                            Point2D center = new Point2D.Double(anchor.getCenterX(), anchor.getCenterY());

                            templateHandler.drawCircleFragment(
                                    slide, center,
                                    mapping.applyProgressAroundImage().getRadius(),
                                    mapping.applyProgressAroundImage().getBorderThickness(),
                                    360,
                                    new Color(231, 230, 230)
                            );
                            if (i < progressList.length) {
                                templateHandler.drawCircleFragment(
                                        slide, center,
                                        mapping.applyProgressAroundImage().getRadius(),
                                        mapping.applyProgressAroundImage().getBorderThickness(),
                                        (double) (progressList[i] * 360) / 100,
                                        mapping.applyProgressAroundImage().getColor()
                                );
                            }
                        }

                        templateHandler.replacePictureShape(shape, newPicturePath, fallbackImagePath);
                    } else {
                        templateHandler.removeShapeByAltText(slide, placeholder);
                    }
                }
            }
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}

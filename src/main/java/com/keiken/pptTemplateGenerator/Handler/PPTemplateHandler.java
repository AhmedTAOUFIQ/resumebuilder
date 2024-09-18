package com.keiken.pptTemplateGenerator.Handler;

import com.keiken.mapper.TemplateBaseMapper;
import org.apache.poi.xslf.usermodel.*;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.List;

public interface PPTemplateHandler {
    XMLSlideShow loadTemplate(String templateFilename) throws IOException;
    byte[] saveAndCloseTemplate(XMLSlideShow ppt) throws IOException;
    String getAltText(XSLFShape shape);
    void removeShapeByAltText(XSLFSlide slide, String altText);
    void replacePictureShape(XSLFPictureShape oldPictureShape, String newPicturePath, String fallbackImagePath);
    void fillImages(List<XSLFPictureShape> shapesToReplace, String[] imagesList, String folderPath, String fallbackImage);
    void fillTable(XSLFTable table, List<TemplateBaseMapper.Fact> facts, int textMaxWords);
     void drawCircleFragment(XSLFSlide slide, Point2D center, double radius, double thicknessMM, double angle, Color color);
     Path2D calculateCircleFragmentPath(Point2D center, double radius, double thicknessPoints, double angle);
   <Obj> void fillTextPlaceholders(XSLFTextParagraph textParagraph, Obj dataObject, int textMaxWords);
}
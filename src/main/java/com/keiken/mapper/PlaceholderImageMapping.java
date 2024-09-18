package com.keiken.mapper;

import com.keiken.pptTemplateGenerator.shapes.ProgressCircle;

public interface PlaceholderImageMapping {
    String getPlaceholdersPattern();
    String getDataSourceMethod();
    String getImageFolderPath();
    String getFallbackImageName();
    int getNumberOfImagesToReplace();
    ProgressCircle applyProgressAroundImage();
    String getProgressPercentagesMethod();
}
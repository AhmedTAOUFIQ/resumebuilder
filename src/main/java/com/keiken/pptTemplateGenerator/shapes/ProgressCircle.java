package com.keiken.pptTemplateGenerator.shapes;

import lombok.*;

import java.awt.*;

@AllArgsConstructor @NoArgsConstructor
@Builder @Getter @Setter
public class ProgressCircle {
    private double radius;
    private Color color;
    private double borderThickness;
}

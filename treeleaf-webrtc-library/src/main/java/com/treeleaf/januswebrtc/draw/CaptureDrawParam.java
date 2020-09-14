package com.treeleaf.januswebrtc.draw;

import androidx.core.graphics.ColorUtils;


public class CaptureDrawParam {

    private Float brushWidth;
    private Integer brushOpacity;
    private Integer brushColor;
    private Boolean clearCanvas;
    private Float xCoordinate, yCoordinate;

    public CaptureDrawParam() {
        this.brushWidth = 8.0F;
        this.brushOpacity = 255;
        this.brushColor = -16777216;
        this.clearCanvas = false;
    }

    public Float getBrushWidth() {
        return this.brushWidth;
    }

    public void setBrushWidth(Float brushWidth) {
        this.brushWidth = brushWidth;
    }

    public Integer getBrushOpacity() {
        return this.brushOpacity;
    }

    public void setBrushOpacity(Integer brushOpacity) {
        this.brushOpacity = brushOpacity;
        this.setBrushColor(this.getBrushColor());
    }

    public Integer getBrushColor() {
        return this.brushColor;
    }

    public void setBrushColor(Integer brushColor) {
        int alphaColor = ColorUtils.setAlphaComponent(brushColor, this.getBrushOpacity());
        this.brushColor = alphaColor;
    }

    public Boolean getClearCanvas() {
        return this.clearCanvas;
    }

    public void setClearCanvas(Boolean clearCanvas) {
        this.clearCanvas = clearCanvas;
    }

    public Float getXCoordinate() {
        return xCoordinate;
    }

    public void setXCoordinate(Float xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public Float getYCoordinate() {
        return yCoordinate;
    }

    public void setYCoordinate(Float yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

}

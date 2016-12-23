package com.web4enterprise.pdf.layout.image;

import java.util.ArrayList;
import java.util.List;

import com.web4enterprise.pdf.core.font.Font;
import com.web4enterprise.pdf.core.geometry.Point;
import com.web4enterprise.pdf.core.page.Page;
import com.web4enterprise.pdf.layout.paragraph.ParagraphElement;
import com.web4enterprise.pdf.layout.paragraph.ParagraphStyle;
import com.web4enterprise.pdf.layout.text.Text;
import com.web4enterprise.pdf.layout.text.TextStyle;

public class Image implements ParagraphElement {	
	protected com.web4enterprise.pdf.core.image.Image coreImage;
	
	public Image(com.web4enterprise.pdf.core.image.Image coreImage) {
		this.coreImage = coreImage.cloneReference();
	}

	public int getWidth() {
		return coreImage.getWidth();
	}
	
	public void setWidth(int width) {
		this.coreImage.setWidth(width);
	}
	
	public void setWidth(int width, boolean keepRatio) {
		float oldWidth = this.coreImage.getWidth();
		this.coreImage.setWidth(width);
		if(keepRatio) {
			this.coreImage.setHeight((int) Math.round((width * this.coreImage.getHeight()) / oldWidth));
		}
	}
	
	public int getHeight() {
		return coreImage.getHeight();
	}
	
	public void setHeight(int height) {
		this.coreImage.setHeight(height);
	}
	
	public void setHeight(int height, boolean keepRatio) {
		float oldHeight = this.coreImage.getHeight();
		this.coreImage.setHeight(height);
		if(keepRatio) {
			this.coreImage.setWidth((int) Math.round((height * this.coreImage.getWidth()) / oldHeight));
		}
	}
	
	@Override
	public List<ParagraphElement> getLines() {
		List<ParagraphElement> lines = new ArrayList<>();
		lines.add(this);
		return lines;
	}
	
	@Override
	public float getWidth(ParagraphStyle defaultStyle, float defaultTextSize) {
		return getWidth();
	}
	
	@Override
	public SplitInformation split(ParagraphStyle defaultStyle, float fontSize,
			float positionX, float firstLineMaxWidth, Float maxWidth) {
		SplitInformation splitInformation = new SplitInformation();
		
		List<ParagraphElement> lines = new ArrayList<>();
		
		//If image does not fit in left space (and if we are not at the start of a line).
		if(positionX != 0 && positionX + getWidth() > firstLineMaxWidth) {
			//Add a new line but with an invisible text to not change previous line shape.
			TextStyle textStyle = new TextStyle(Font.TIMES_ROMAN, 0);
			lines.add(new Text(textStyle, ""));
			positionX = 0;
		}
		
		lines.add(this);
		
		splitInformation.positionX = getWidth();
		splitInformation.splitElements = lines;
		return splitInformation;
	}
	
	@Override
	public Point layout(Page page, ParagraphStyle defaultStyle,
			float defaultFontSize, float positionX, float positionY) {
		coreImage.setX(positionX);
		coreImage.setY(positionY);
		
		page.add(coreImage);
		
		return new Point(coreImage.getWidth(), coreImage.getHeight());
	}	

	@Override
	public float getLineSpacing(ParagraphStyle defaultStyle) {
		return defaultStyle.getFontSize() * defaultStyle.getLineSpacing();
	}
	
	@Override
	public Image clone() {
		return new Image(coreImage);
	}
}

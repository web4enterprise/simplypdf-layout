package com.web4enterprise.pdf.layout.text;

import java.util.ArrayList;
import java.util.List;

import com.web4enterprise.pdf.core.font.Font;
import com.web4enterprise.pdf.core.font.FontVariant;
import com.web4enterprise.pdf.core.geometry.Point;
import com.web4enterprise.pdf.core.page.Page;
import com.web4enterprise.pdf.core.styling.Color;
import com.web4enterprise.pdf.layout.paragraph.ParagraphStyle;
import com.web4enterprise.pdf.layout.paragraph.ParagraphElement;

public class Text implements ParagraphElement {
	public static final String NEW_LINE = "\n";	
	public static final Text NEW_TEXT_LINE = new Text(NEW_LINE);

	protected TextStyle style = new TextStyle();
	protected String string;
	
	public Text(String string) {
		this.string = string;
	}
	
	public Text(TextStyle style, String string) {
		this.style = style;
		this.string = string;
	}

	public TextStyle getStyle() {
		return style;
	}

	public void setStyle(TextStyle style) {
		this.style = style;
	}

	public String getString() {
		return string;
	}

	public void setString(String string) {
		this.string = string;
	}
	
	public List<ParagraphElement> getLines() {
		ArrayList<ParagraphElement> lines = new ArrayList<>();

		//If string starts with a new line, we have to create an empty one.
		if(string.startsWith(NEW_LINE)) {
			lines.add(new Text(style, ""));
		}
		
		//Split will return only one entry if regex if at start or end of string.
		String[] stringLines = string.split(NEW_LINE);
		for(String stringLine : stringLines) {
			lines.add(new Text(style, stringLine));
		}
		
		//If string ends with a new line, we have to create an empty one.
		if(string.endsWith(NEW_LINE)) {
			lines.add(new Text(style, ""));
		}
		
		return lines;
	}
	
	@Override
	public SplitInformation split(ParagraphStyle defaultStyle, int fontSize, float positionX, float firstLineMaxWidth, Float maxWidth) {
		TextStyle textStyle = getStyle();
		//Get font name between paragraph and text ones.
		Font currentFont = (style.getFont() != null)?style.getFont():defaultStyle.getFont();
		FontVariant currentFontVariant = currentFont.getVariant((style.getFontStyle() != null)?
				style.getFontStyle():defaultStyle.getFontStyle());
		//Get font size between paragraph and text ones.
		int currentTextSize = (textStyle.getFontSize() != null)?textStyle.getFontSize():fontSize;
		
		//Split the text on max width.
		SplitInformation splitInformation = new SplitInformation();
		split(string, currentFontVariant, currentTextSize, positionX, firstLineMaxWidth, maxWidth, splitInformation, true);
		
		return splitInformation;
	}
	
	public void split(String stringToSplit, FontVariant font, int fontSize, float positionX, float firstLineMaxWidth, 
			float maxWidth, SplitInformation splitInformation, boolean isFirstLine) {
		float textWidth = font.getWidth(fontSize, stringToSplit);
		String keptString = stringToSplit;
		
		float currentMaxWidth = maxWidth;
		if(isFirstLine) {
			currentMaxWidth = firstLineMaxWidth;
		}
		currentMaxWidth -= positionX;
		
		//If we have to split.
		while(textWidth > currentMaxWidth) {
			//Get index of split
			int splitIndex = keptString.lastIndexOf(' ');
			//If string cannot be split on space, we need to cut it inside a word to fit required width.
			if(splitIndex == 0 || splitIndex == -1) {
				boolean needSplit = true;
				int currentSplitIndex = stringToSplit.length();
				while(needSplit) {
					currentSplitIndex--;
					keptString = stringToSplit.substring(0, currentSplitIndex);
					
					textWidth = font.getWidth(fontSize, keptString);
					//If split is enough or if we don't have anything to split.
					if(textWidth <= currentMaxWidth || stringToSplit.length() == 1) {
						needSplit = false;
					}
				}
			} else {
				keptString = stringToSplit.substring(0, splitIndex);
				textWidth = font.getWidth(fontSize, keptString);
			}
		}
		
		//Add information on current line to split information.
		splitInformation.splitElements.add(new Text(style, keptString));
		splitInformation.positionX = textWidth;
		
		//Try to split the text left.
		if(stringToSplit.length() != keptString.length()) {
			String textLeft = stringToSplit.substring(keptString.length());
			//We split on space earlier, we don't want to display it on new line, so remove it.
			split(textLeft.substring(1), font, fontSize, 0, firstLineMaxWidth, maxWidth, splitInformation, false);
		}
	}
	
	@Override
	public float getWidth(ParagraphStyle defaultStyle, int defaultTextSize) {
		Font currentFont = (style.getFont() != null)?style.getFont():defaultStyle.getFont();
		FontVariant currentFontVariant = currentFont.getVariant((style.getFontStyle() != null)?
				style.getFontStyle():defaultStyle.getFontStyle());
		int currentTextSize = (style.fontSize != null)?style.fontSize:defaultTextSize;
		
		return currentFontVariant.getWidth(currentTextSize, string);
	}
	
	@Override
	public Point layout(Page page, ParagraphStyle defaultStyle, int defaultFontSize, float positionX, float positionY) {
		Font currentFont = (style.getFont() != null)?style.getFont():defaultStyle.getFont();
		FontVariant currentFontVariant = currentFont.getVariant((style.getFontStyle() != null)?
				style.getFontStyle():defaultStyle.getFontStyle());
		int currentFontSize = (style.fontSize != null)?style.fontSize:defaultFontSize;
		
		Color color = (style.getFontColor() != null)?style.getFontColor():defaultStyle.getFontColor();
		
		Boolean isUnderlined = (style.isUnderlined != null)?style.isUnderlined:defaultStyle.isUnderlined();
		com.web4enterprise.pdf.core.text.Text text = new com.web4enterprise.pdf.core.text.Text(positionX, positionY, currentFontSize, currentFontVariant, color, string);
		if(Boolean.TRUE.equals(isUnderlined)) {
			text.setUnderlined(isUnderlined);
			Color underlineColor = (style.getUnderlineColor() != null)?style.getUnderlineColor():defaultStyle.getUnderlineColor();
			text.setUnderlineColor(underlineColor);
		}
		
		page.addText(text);
		
		float width = currentFontVariant.getWidth(currentFontSize, string);
		
		return new Point(width, currentFontSize);
	}

	@Override
	public float getLineSpacing(ParagraphStyle defaultStyle) {
		float textHeight = defaultStyle.getFontVariant().getHeight(defaultStyle.getFontSize());
		
		return textHeight * defaultStyle.getLineSpacing();
	}
}

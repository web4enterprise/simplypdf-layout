/*
 * Copyright 2017 web4enterprise.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.web4enterprise.pdf.layout.text;

import com.web4enterprise.pdf.core.font.Font;
import com.web4enterprise.pdf.core.font.FontsVariant;
import com.web4enterprise.pdf.core.styling.Color;
import com.web4enterprise.pdf.core.text.TextScript;
import com.web4enterprise.pdf.layout.style.Style;

public class TextStyle implements Style, Cloneable {
	protected Font font;
	protected FontsVariant fontVariant;
	protected Float fontSize;
	protected Color fontColor;
	protected Boolean underlined;
	protected Color underlineColor;
	protected TextScript script = TextScript.NORMAL;
	
	public TextStyle() {
	}
	
	public TextStyle(Font font) {
		this.font = font;
	}
	
	public TextStyle(FontsVariant fontVariant) {
		this.fontVariant = fontVariant;
	}
	
	public TextStyle(Font font, float fontSize) {
		this.font = font;
		this.fontSize = fontSize;
	}
	
	public TextStyle(Font font, FontsVariant fontVariant, float fontSize) {
		this.font = font;
		this.fontVariant = fontVariant;
		this.fontSize = fontSize;
	}
	
	public TextStyle(Font font, FontsVariant fontVariant, float fontSize, Color fontColor) {
		this.font = font;
		this.fontVariant = fontVariant;
		this.fontSize = fontSize;
		this.fontColor = fontColor;
	}
	
	public Font getFont() {
		return font;
	}
	public void setFont(Font font) {
		this.font = font;
	}
	
	public FontsVariant getFontStyle() {
		return fontVariant;
	}

	public void setFontStyle(FontsVariant fontVariant) {
		this.fontVariant = fontVariant;
	}

	public Float getFontSize() {
		return fontSize;
	}

	public void setFontSize(Float fontSize) {
		this.fontSize = fontSize;
	}
	
	public Color getFontColor() {
		return fontColor;
	}

	public void setFontColor(Color fontColor) {
		this.fontColor = fontColor;
	}

	public Boolean isUnderlined() {
		return underlined;
	}
	
	public void setUnderlined(Boolean isUnderlined) {
		this.underlined = isUnderlined;
	}

	public Color getUnderlineColor() {
		return underlineColor;
	}

	public void setUnderlineColor(Color underlineColor) {
		this.underlineColor = underlineColor;
	}

	public TextScript getScript() {
		return script;
	}

	public void setScript(TextScript script) {
		this.script = script;
	}
	
	@Override
	public TextStyle clone() {
		TextStyle clone = new TextStyle(font, fontVariant, fontSize, fontColor);
		clone.setScript(script);
		clone.setUnderlineColor(underlineColor);
		clone.setUnderlined(underlined);
		
		return clone;
	}
}

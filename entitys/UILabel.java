package eu.gir.girsignals.guis.guilib.entitys;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)

public class UILabel extends UIComponent {

	public static final int DEFAULT_STRING_COLOR = 4210752;
	private String string;
	private int stringColor;
	private FontRenderer renderer;
	private int restHeight = 0;
	private int restWidth = 0;

	public UILabel(String text) {
		this.string = text;
		this.stringColor = DEFAULT_STRING_COLOR;
		this.renderer = Minecraft.getMinecraft().fontRenderer;
	}

	@Override
	public void draw(int mouseX, int mouseY) {
		if (this.visible) {
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
					GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
					GlStateManager.DestFactor.ZERO);
			renderer.drawString(string, restWidth, restHeight, stringColor);
		}
	}

	@Override
	public void update() {
		if (this.parent.inheritHeight()) {
			this.parent.height =  renderer.FONT_HEIGHT;
		}
		this.restHeight = (this.parent.getHeight() - renderer.FONT_HEIGHT) / 2;
		if (this.parent.inheritWidth()) {
			this.parent.width = Math.max(renderer.getStringWidth(string), parent.getWidth());
		}
		this.restWidth = (this.parent.getWidth() - renderer.getStringWidth(string)) / 2;
	}

	public String getText() {
		return string;
	}

	public void setText(String string) {
		this.string = string;
	}

	public int getTextColor() {
		return stringColor;
	}

	public void setTextColor(int stringColor) {
		this.stringColor = stringColor;
	}

}

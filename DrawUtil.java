package eu.gir.girsignals.guis.guilib;

import static net.minecraft.client.gui.Gui.drawRect;
import static net.minecraft.client.gui.Gui.drawScaledCustomSizeModalRect;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.pipeline.LightUtil;
import net.minecraftforge.common.property.IExtendedBlockState;

public class DrawUtil {
	
	private static final ResourceLocation CREATIVE_INVENTORY_TABS = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");
	public static final float DIM = 256.0f;
	
	public static class DisableIntegerable<T extends Comparable<T>> implements IIntegerable<T> {

		private final IIntegerable<T> integerable;
		
		public DisableIntegerable(IIntegerable<T> integerable) {
			this.integerable = integerable;
		}
		
		@Override
		public T getObjFromID(int obj) {
			return integerable.getObjFromID(obj);
		}

		@Override
		public int count() {
			return integerable.count();
		}

		@Override
		public String getName() {
			return integerable.getName();
		}
		
		@Override
		public String getNamedObj(int obj) {
			if(obj < 0)
				return getLocalizedName() + ": " + I18n.format("property.disabled.name");
			return integerable.getNamedObj(obj);
		}
		
	}
	
	public static class EnumIntegerable<T extends Enum<T>> implements IIntegerable<T> {
		
		private Class<T> t;
		
		public EnumIntegerable(Class<T> t) {
			this.t = t;
		}
		
		@Override
		public T getObjFromID(int obj) {
			return t.getEnumConstants()[obj];
		}
		
		@Override
		public int count() {
			return t.getEnumConstants().length;
		}
		
		@Override
		public String getName() {
			return t.getSimpleName().toLowerCase();
		}
	}
	
	public static class NamedEnumIntegerable<T extends Enum<T>> extends EnumIntegerable<T> {
		
		private final String name;
		
		public NamedEnumIntegerable(final String name, Class<T> t) {
			super(t);
			this.name = name;
		}
		
		@Override
		public String getName() {
			return name;
		}
		
	}
	
	public interface ObjGetter<D> {
		
		D getObjFrom(int x);
	}
	
	public static class SizeIntegerables<T> implements IIntegerable<T> {
		
		private final int count;
		private final ObjGetter<T> getter;
		private final String name;
		
		public SizeIntegerables(final String name, final int count, final ObjGetter<T> getter) {
			this.count = count;
			this.getter = getter;
			this.name = name;
		}
		
		@Override
		public T getObjFromID(int obj) {
			return this.getter.getObjFrom(obj);
		}
		
		@Override
		public int count() {
			return count;
		}
		
		public static <T> IIntegerable<T> of(final String name, final int count, final ObjGetter<T> get) {
			return new SizeIntegerables<T>(name, count, get);
		}
		
		@Override
		public String getName() {
			return this.name;
		}
		
		@Override
		public String getNamedObj(int obj) {
			return getLocalizedName() + ": " + obj;
		}
	}
	
	public static class BoolIntegerables implements IIntegerable<String> {
		
		private final String name;
		
		public BoolIntegerables(final String name) {
			this.name = name;
		}
		
		@Override
		public String getObjFromID(int obj) {
			return obj == 0 ? "true" : "false";
		}
		
		@Override
		public int count() {
			return 2;
		}
		
		public static BoolIntegerables of(final String name) {
			return new BoolIntegerables(name);
		}
		
		@Override
		public String getName() {
			return this.name;
		}
		
	}
	
	public static class IntegerHolder {
		
		private int obj;
		
		public IntegerHolder(final int i) {
			this.setObj(i);
		}
		
		public int getObj() {
			return obj;
		}
		
		public void setObj(int obj) {
			this.obj = obj;
		}
		
	}
	
	public static void drawBack(GuiScreen gui, final int xLeft, final int xRight, final int yTop, final int yBottom) {
		gui.mc.getTextureManager().bindTexture(CREATIVE_INVENTORY_TABS);
		
		gui.drawTexturedModalRect(xLeft, yTop, 0, 32, 4, 4);
		gui.drawTexturedModalRect(xLeft, yBottom, 0, 124, 4, 4);
		gui.drawTexturedModalRect(xRight, yTop, 24, 32, 4, 4);
		gui.drawTexturedModalRect(xRight, yBottom, 24, 124, 4, 4);
		
		drawScaledCustomSizeModalRect(xLeft + 4, yBottom, 4, 124, 1, 4, xRight - 4 - xLeft, 4, DIM, DIM);
		drawScaledCustomSizeModalRect(xLeft + 4, yTop, 4, 32, 1, 4, xRight - 4 - xLeft, 4, DIM, DIM);
		drawScaledCustomSizeModalRect(xLeft, yTop + 4, 0, 36, 4, 1, 4, yBottom - 4 - yTop, DIM, DIM);
		drawScaledCustomSizeModalRect(xRight, yTop + 4, 24, 36, 4, 1, 4, yBottom - 4 - yTop, DIM, DIM);
		
		drawRect(xLeft + 4, yTop + 4, xRight, yBottom, 0xFFC6C6C6);
	}
	
	public static void draw(BufferBuilder bufferBuilderIn) {
		if (bufferBuilderIn.getVertexCount() > 0) {
			VertexFormat vertexformat = bufferBuilderIn.getVertexFormat();
			int i = vertexformat.getNextOffset();
			ByteBuffer bytebuffer = bufferBuilderIn.getByteBuffer();
			List<VertexFormatElement> list = vertexformat.getElements();
			
			for (int j = 0; j < list.size(); ++j) {
				VertexFormatElement vertexformatelement = list.get(j);
				bytebuffer.position(vertexformat.getOffset(j));
				vertexformatelement.getUsage().preDraw(vertexformat, j, i, bytebuffer);
			}
			
			GlStateManager.glDrawArrays(bufferBuilderIn.getDrawMode(), 0, bufferBuilderIn.getVertexCount());
			int i1 = 0;
			
			for (int j1 = list.size(); i1 < j1; ++i1) {
				VertexFormatElement vertexformatelement1 = list.get(i1);
				vertexformatelement1.getUsage().postDraw(vertexformat, i1, i, bytebuffer);
			}
		}
	}
	
	public static void addToBuffer(BufferBuilder builder, BlockModelShapes manager, IBlockState ebs) {
		addToBuffer(builder, manager, ebs, 0);
	}
	
	public static void addToBuffer(BufferBuilder builder, BlockModelShapes manager, IBlockState ebs, int color) {
		assert ebs != null;
		IBlockState cleanState = ebs instanceof IExtendedBlockState ? ((IExtendedBlockState) ebs).getClean() : ebs;
		IBakedModel mdl = manager.getModelForState(cleanState);
		List<BakedQuad> lst = new ArrayList<>();
		lst.addAll(mdl.getQuads(ebs, null, 0));
		for (EnumFacing face : EnumFacing.VALUES)
			lst.addAll(mdl.getQuads(ebs, face, 0));
		
		final BlockColors blockColors = Minecraft.getMinecraft().getBlockColors();
		for (BakedQuad quad : lst) {
			final int k = quad.hasTintIndex() ? (blockColors.colorMultiplier(cleanState, null, null, quad.getTintIndex()) + 0xFF000000) : 0xFFFFFFFF;
			LightUtil.renderQuadColor(builder, quad, color + k);
		}
	}
	
	public static void drawCenteredString(FontRenderer fontRendererIn, String text, int x, int y, int color) {
		fontRendererIn.drawStringWithShadow(text, (float) (x - fontRendererIn.getStringWidth(text) / 2), (float) y, color);
	}
}

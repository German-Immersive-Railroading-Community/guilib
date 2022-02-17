package eu.gir.guilib.ecs.entitys.input;

import java.util.function.IntConsumer;

import eu.gir.guilib.ecs.entitys.UIComponent;
import eu.gir.guilib.ecs.entitys.UIEntity.EnumMouseState;
import eu.gir.guilib.ecs.entitys.UIEntity.MouseEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class UIScroll extends UIComponent {
	
	private IntConsumer consumer;
	
	public UIScroll(IntConsumer consumer) {
		this.consumer = consumer;
	}
	
	@Override
	public void draw(int mouseX, int mouseY) {
	}
	
	@Override
	public void update() {
		
	}
	
	@Override
	public void mouseEvent(MouseEvent event) {
		if (event.state.equals(EnumMouseState.SCROLL) && parent.isHovered()) {
			consumer.accept(event.x);
		}
	}
	
}
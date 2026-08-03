package functionhook.oldwu.client.model;

import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.animal.Cat;
import org.joml.Vector3f;

/** Shared 1.21.1 model base replacing the 26.2 RenderState model base. */
public abstract class OldWuCatModel extends HierarchicalModel<Cat> {
    private static final Vector3f ANIMATION_VECTOR_CACHE = new Vector3f();
    protected final ModelPart root;

    protected OldWuCatModel(ModelPart root) {
        this.root = root;
    }

    @Override
    public ModelPart root() {
        return root;
    }

    /**
     * The 26.2 EntityModel base reset every baked part from its initial pose at
     * the start of setupAnim. 1.21.1 HierarchicalModel does not do that for
     * these custom models, so additive idle/keyframe rotations would otherwise
     * accumulate every rendered frame.
     */
    protected final void oldwuResetPose() {
        this.root.getAllParts().forEach(ModelPart::resetPose);
    }

    protected final void oldwuAnimate(AnimationDefinition definition, float ageInTicks) {
        KeyframeAnimations.animate(this, definition, (long) (ageInTicks * 50.0F), 1.0F, ANIMATION_VECTOR_CACHE);
    }
}

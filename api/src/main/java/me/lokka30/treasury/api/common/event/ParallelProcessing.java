/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.api.common.event;

import com.google.common.annotations.Beta;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Tells the {@link EventBus} that for the annotated event can process the
 * {@link EventSubscriber} calls in parallel.
 * <p><b>WARNING: </b>This annotation is not applicable to {@link Cancellable} events.
 * <b>Applying this annotation onto a cancellable event will result in {@link FireCompletion}
 * with an exception when {@link EventBus#fire(Object)} is called.</b>
 *
 * @author MrIvanPlays
 * @since 2.0.0
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Beta
public @interface ParallelProcessing {

}

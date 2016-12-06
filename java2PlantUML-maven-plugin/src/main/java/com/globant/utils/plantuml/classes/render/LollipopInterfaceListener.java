package com.globant.utils.plantuml.classes.render;

import com.globant.utils.plantuml.classes.render.event.RejectingBaseInterfaceEvent;
import com.globant.utils.plantuml.classes.render.filters.Filters;
import com.globant.utils.plantuml.classes.structure.Implementation;
import com.google.common.eventbus.Subscribe;

/**
 * @author juanmf@gmail.com
 * @modified efrain.calla
 */
public class LollipopInterfaceListener {
    @Subscribe
    public void handle(RejectingBaseInterfaceEvent e) {
        StringBuilder sb = e.getScriptStringBuilder();
        try {
            Implementation imp = (Implementation) e.getFilteringObject();
            if (! Filters.FILTER_RELATION_FORBID_FROM_BASE.satisfy(imp, sb)) {
                return;
            }
            sb.append(imp.asLollipop()).append("\n");
        } catch (ClassCastException ex) {
            // do nothing
        }
    }
}

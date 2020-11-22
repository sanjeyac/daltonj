package eu.sanprojects.daltonj.filters;

import eu.sanprojects.daltonj.filters.impl.AbstractFilter;
import eu.sanprojects.daltonj.filters.impl.GrayscaleFilter;
import eu.sanprojects.daltonj.filters.impl.RedGreenFilter;
import eu.sanprojects.daltonj.filters.impl.TritanFilter;

/**
 * Facade for filters
 */
public class Filters {

    public static final AbstractFilter GRAY_SCALE = new GrayscaleFilter();

    public static final AbstractFilter DEUTERANOPIA = new RedGreenFilter(9591, 23173, -730);

    public static final AbstractFilter PROTANOPIA = new RedGreenFilter(3683, 29084, 131);

    public static final AbstractFilter TRITANOPIA = new TritanFilter();

}

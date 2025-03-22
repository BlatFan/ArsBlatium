package ru.blatfan.ars_blatium.spells;

public class BlatiumGlyphes {
    public static final String hole = reg("blatium_hole");
    public static final String small_hole = reg("small_blatium_hole");
    
    private static String reg(String glyph) {
        return "glyph_" + glyph;
    }
}

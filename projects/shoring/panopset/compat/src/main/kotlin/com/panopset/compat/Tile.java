package com.panopset.compat;

import java.io.StringWriter;

public class Tile {

    private Tile[] splitz;
    private Splitter splitter;
    private int x;
    private int y;
    private int w;
    private int h;
    private int centerW;
    private int centerH;
    private int r;
    private int b;
    private String s;
    private final String nm;

    public Tile(final String name) {
        nm = name;
    }

    public String getName() {
        return nm;
    }

    public String toString() {
        if (s == null) {
            StringWriter sw = new StringWriter();
            sw.append(nm);
            sw.append(":xyrbwh:").append(String.valueOf(x));
            sw.append(",").append(String.valueOf(y));
            sw.append(",").append(String.valueOf(r));
            sw.append(",").append(String.valueOf(b));
            sw.append(",").append(String.valueOf(w));
            sw.append(",").append(String.valueOf(h));
            s = sw.toString();
        }
        return s;
    }

    private void initSplitz(final String name0, final String name1) {
        if (isSplit()) {
            throw new RuntimeException("Can't split twice.");
        }
        splitz = new Tile[2];
        splitz[0] = new Tile(name0);
        splitz[1] = new Tile(name1);
    }

    public Tile[] splitHorizontal(int pct, final String name0, final String name1) {
        initSplitz(name0, name1);
        splitter = new PercentSplitter(pct) {

            @Override
            public boolean isHorizontalSplit() {
                return true;
            }

        };
        updateSplitz();
        return splitz;
    }

    public Tile[] splitVertical(int pct, final String name0, final String name1) {
        initSplitz(name0, name1);
        splitter = new PercentSplitter(pct) {

            @Override
            public boolean isHorizontalSplit() {
                return false;
            }

        };
        updateSplitz();
        return splitz;
    }

    public Tile[] splitVerticalExactHeight(int pixels, final String name0, final String name1) {
        initSplitz(name0, name1);
        splitter = new ExactSplitter(pixels) {

            @Override
            public boolean isHorizontalSplit() {
                return false;
            }

        };
        updateSplitz();
        return splitz;
    }

    public Tile[] splitHorizontalExactWidth(int pixels, final String name0, final String name1) {
        initSplitz(name0, name1);
        splitter = new ExactSplitter(pixels) {

            @Override
            public boolean isHorizontalSplit() {
                return true;
            }

        };
        updateSplitz();
        return splitz;
    }

    public boolean isSplit() {
        return splitz != null;
    }

    private void updateHorizontalSplit() {
        Tile left = splitz[0];
        Tile right = splitz[1];
        int[] widths = split(w);
        left.setLocation(x, y);
        left.setDimensions(widths[0], h);
        right.setLocation(x + widths[0], y);
        right.setDimensions(widths[1], h);
        right.updateSplitz();
        left.updateSplitz();
    }

    private int[] split(int span) {
        return splitter.split(span);
    }

    private void updateVerticalSplit() {
        Tile top = splitz[0];
        Tile bottom = splitz[1];
        int[] heights = split(h);
        top.setLocation(x, y);
        top.setDimensions(w, heights[0]);
        bottom.setLocation(x, y + heights[0]);
        bottom.setDimensions(w, heights[1]);
        top.updateSplitz();
        bottom.updateSplitz();
    }

    private void updateSplitz() {
        if (!isSplit()) {
            return;
        }
        if (splitter.isHorizontalSplit()) {
            updateHorizontalSplit();
        } else {
            updateVerticalSplit();
        }
    }

    public Tile setRectXYWH(int xloc, int yloc, int width, int height) {
        setLocation(xloc, yloc);
        setDimensions(width, height);
        return this;
    }

    public Tile setRectLTRB(int left, int top, int right, int bottom) {
        setLocation(left, top);
        setDimensions(right - left, bottom - top);
        return this;
    }

    public Tile setLocation(int xloc, int yloc) {
        x = xloc;
        y = yloc;
        return this;
    }

    public Tile setDimensions(int width, int height) {
        w = width;
        h = height;
        return calc();
    }

    private Tile calc() {
        s = null;
        b = y + h;
        r = x + w;
        centerW = x + (w / 2);
        centerH = y + (h / 2);
        updateSplitz();
        return this;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return w;
    }

    public int getCenterW() {
        return centerW;
    }

    public int getCenterH() {
        return centerH;
    }

    public int getHeight() {
        return h;
    }

    public int getLeft() {
        return x;
    }

    public int getRight() {
        return r;
    }

    public int getTop() {
        return y;
    }

    public int getBottom() {
        return b;
    }

    abstract static class PercentSplitter implements Splitter {
        private final int pct;

        public PercentSplitter(int percent) {
            if (percent > 100) {
                throw new ArrayIndexOutOfBoundsException(percent);
            }
            pct = percent;
        }

        public int[] split(int span) {
            int rtn0 = (span * pct) / 100;
            return new int[]{rtn0, span - rtn0};
        }
    }

    abstract static class ExactSplitter implements Splitter {
        private final int pxs;

        public ExactSplitter(int pixels) {
            pxs = pixels;
        }

        public int[] split(int span) {
            return new int[]{pxs, span - pxs};
        }
    }

    interface Splitter {
        boolean isHorizontalSplit();

        int[] split(int span);
    }

}

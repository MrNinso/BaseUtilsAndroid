package com.developer.base.utils.android;

import com.developer.base.utils.android.tools.data.MaskString;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class MaskStringTests {
    @Test
    public void mask() {
        MaskString m = new MaskString("##$(##//# #!#");

        Assert.assertEquals("1", m.mask("1"));
        Assert.assertEquals("12", m.mask("12"));
        Assert.assertEquals("12$(3", m.mask("123"));
        Assert.assertEquals("12$(34", m.mask("1234"));
        Assert.assertEquals("12$(34//5", m.mask("12345"));
        Assert.assertEquals("12$(34//5 6", m.mask("123456"));
        Assert.assertEquals("12$(34//5 6!7", m.mask("1234567"));
        Assert.assertEquals("12$(34//5 6!7", m.mask("1234567"));
        Assert.assertEquals("12$(34//5 6!7", m.mask("12345678"));
        Assert.assertEquals("12$(34//5 6!7", m.mask("12$(34//5 6!7"));
        Assert.assertEquals("12$(34//5 6", m.mask("12$(34//5 6!"));
        Assert.assertEquals("12$(34//5", m.mask("12$(34//5 "));
        Assert.assertEquals("12$(34", m.mask("12$(34//"));
        Assert.assertEquals("12$(3", m.mask("12$(3"));
        Assert.assertEquals("12", m.mask("12$("));
    }

    @Test
    public void unmask() {
        MaskString m = new MaskString("@@$(@@//@ @!@", '@');

        Assert.assertEquals("1", m.unmask("1"));
        Assert.assertEquals("12", m.unmask("12"));
        Assert.assertEquals("123", m.unmask("12$(3"));
        Assert.assertEquals("1234", m.unmask("12$(34"));
        Assert.assertEquals("12345", m.unmask("12$(34//5"));
        Assert.assertEquals("123456", m.unmask("12$(34//5 6"));
        Assert.assertEquals("1234567", m.unmask("12$(34//5 6!7"));
    }
}

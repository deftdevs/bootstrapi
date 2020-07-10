package de.aservo.confapi.commons.util;

import org.junit.Assert;
import org.junit.Test;

public class ConversionUtilTest {

    @Test
    public void testConvertLong() {
        Assert.assertEquals(0, ConversionUtil.toLong(null));
        Assert.assertEquals(0, ConversionUtil.toLong(""));
        Assert.assertEquals(100, ConversionUtil.toLong("100"));
        Assert.assertEquals(234234234234234L, ConversionUtil.toLong("234234234234234"));
    }

    @Test
    public void testConvertInt() {
        Assert.assertEquals(0, ConversionUtil.toInt(null));
        Assert.assertEquals(0, ConversionUtil.toInt(""));
        Assert.assertEquals(100, ConversionUtil.toInt("100"));
    }

    @Test
    public void testConvertDouble() {
        Assert.assertEquals(0, ConversionUtil.toDouble(null), 0);
        Assert.assertEquals(0, ConversionUtil.toDouble(""), 0);
        Assert.assertEquals(100, ConversionUtil.toDouble("100"), 0);
        Assert.assertEquals(100.1231234, ConversionUtil.toDouble("100.1231234"), 0);
    }

    @Test
    public void testConvertBoolean() {
        Assert.assertFalse(ConversionUtil.toBoolean(null));
        Assert.assertFalse(ConversionUtil.toBoolean(""));
        Assert.assertFalse(ConversionUtil.toBoolean("false"));
        Assert.assertFalse(ConversionUtil.toBoolean("False"));
        Assert.assertFalse(ConversionUtil.toBoolean("no"));
        Assert.assertFalse(ConversionUtil.toBoolean("0"));
        Assert.assertTrue(ConversionUtil.toBoolean("1"));
        Assert.assertTrue(ConversionUtil.toBoolean("true"));
        Assert.assertTrue(ConversionUtil.toBoolean("TRUE"));
        Assert.assertTrue(ConversionUtil.toBoolean("yes"));
        Assert.assertTrue(ConversionUtil.toBoolean("ja"));
    }
}

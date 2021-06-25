package com.developer.base.utils.android;

import com.developer.base.utils.android.tools.data.validators.CpfCnpjValidator;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ValidatorsTests {
    @Test
    public void ValidCPF() {
        Assert.assertTrue(CpfCnpjValidator.isCPFValid("88758834087"));
    }

    @Test
    public void InValidCPF() {
        Assert.assertFalse(CpfCnpjValidator.isCPFValid("00000000000"));
        Assert.assertFalse(CpfCnpjValidator.isCPFValid("99999999999"));
        Assert.assertFalse(CpfCnpjValidator.isCPFValid("98758834087"));
        Assert.assertFalse(CpfCnpjValidator.isCPFValid("987"));
        Assert.assertFalse(CpfCnpjValidator.isCPFValid(""));
    }
}

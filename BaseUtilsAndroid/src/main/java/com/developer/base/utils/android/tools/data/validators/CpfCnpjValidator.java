package com.developer.base.utils.android.tools.data.validators;

public class CpfCnpjValidator {
    public static boolean isCPFValid(String cpf) {
        if (cpf.length() != 11)
            return false;

        switch (cpf) {
            case "00000000000":
            case "11111111111":
            case "22222222222":
            case "33333333333":
            case "44444444444":
            case "55555555555":
            case "66666666666":
            case "77777777777":
            case "88888888888":
            case "99999999999":
                return false;
        }

        char dig10, dig11;
        int sm = 0 , r, num, peso = 10;

        for (int i = 0; i < 9; i++) {
            num = cpf.charAt(i) - 48;
            sm = sm + (num * peso);

            peso--;
        }

        r = 11 - (sm % 11);

        dig10 = ((r == 10) || (r == 11)) ? '0' : (char)(r +48);

        sm = 0;
        peso = 11;


        for (int i = 0; i < 10; i++) {
            num = cpf.charAt(i) - 48;
            sm = sm + (num * peso);
            peso--;
        }

        r = 11 - (sm % 11);

        dig11 = ((r == 10) || (r == 11)) ? '0' : (char)(r +48);

        return dig10 == cpf.charAt(9) && dig11 == cpf.charAt(10);
    }

    public static boolean isCNPJValid(String cnpj) {        //TODO
        return false;
    }
}

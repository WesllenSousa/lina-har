package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Wesllen Sousa Lima
 */
public class Validation {

    public static boolean hasLetraAlfabeto(String palavra) {
        Pattern p = Pattern.compile("[a-zA-Z]");
        return p.matcher(palavra).find();
    }

    //Verifica se a String é vazia, tem apenas um espaço ou é nula;
    public static boolean isEmptyString(String v) {
        return (v == null) || (v.length() == 0);
    }

    //Verifica se o número é negativo
    public static boolean isNegativeNumber(String v) {
        return Double.parseDouble(v) < 0;
    }

    //Verifica se o número é inteiro
    public static boolean isInteger(String v) {
        try {
            Integer.parseInt(v);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    //Verifica se o número é real do tipo Float
    public static boolean isFloat(String v) {
        try {
            Float.parseFloat(v);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    //Verifica se o número é real do tipo Double
    public static boolean isDouble(String v) {
        try {
            Double.parseDouble(v);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    //Verifica se é um endereço ip válido
    public static boolean isIP(String v) {
        if (v.startsWith(".") || v.endsWith(".")) {
            return false;
        }
        if (v.indexOf(" ") >= 0 || v.indexOf("\t") >= 0) {
            return false;
        }
        String[] parts = v.split("\\.");
        if (parts.length > 4) {
            return false;
        }
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            if (i == parts.length - 1 && part.indexOf(":") > 0) { //NOI18N
                String[] pts = part.split(":"); //NOI18N
                try {
                    int addr = Integer.parseInt(pts[0]);
                    if (addr < 0) {
                        return false;
                    }
                    if (addr > 256) {
                        return false;
                    }
                } catch (NumberFormatException e) {
                    return false;
                }
                if (pts.length == 2 && pts[1].length() == 0) {
                    return false;
                }
                if (pts.length > 1) {
                    try {
                        int port = Integer.parseInt(pts[1]);
                        if (port < 0) {
                            return false;
                        } else if (port >= 65536) {
                            return false;
                        }
                    } catch (NumberFormatException e) {
                        return false;
                    }
                }
            } else {
                try {
                    int addr = Integer.parseInt(part);
                    if (addr < 0) {
                        return false;
                    }
                    if (addr > 256) {
                        return false;
                    }
                } catch (NumberFormatException e) {
                    return false;
                }
            }
        }
        return true;
    }

    //Verifica se a string inicia com número
    public static boolean verificaInicioNumero(String v) {
        if (v.length() > 0) {
            char c = v.charAt(0);
            if (Character.isDigit(c)) {
                return true;
            }
        }
        return false;
    }

    //Verifica se é um email válido
    public static boolean isEmail(String email) {
        Pattern p = Pattern.compile("^[\\w-]+(\\.[\\w-]+)*@([\\w-]+\\.)+[a-zA-Z]{2,7}$");
        Matcher m = p.matcher(email);
        return m.find();
    }

    //Verifica se é um cpf válido
    public static boolean isCPF(String cpf) {
        int d1, d2;
        int digito1, digito2, resto;
        int digitoCPF;
        String nDigResult;

        d1 = d2 = 0;
        digito1 = digito2 = resto = 0;

        for (int nCount = 1; nCount < cpf.length() - 1; nCount++) {
            digitoCPF = Integer.valueOf(cpf.substring(nCount - 1, nCount)).intValue();

            //multiplique a ultima casa por 2 a seguinte por 3 a seguinte por 4 e assim por diante.
            d1 = d1 + (11 - nCount) * digitoCPF;

            //para o segundo digito repita o procedimento incluindo o primeiro digito calculado no passo anterior.
            d2 = d2 + (12 - nCount) * digitoCPF;
        }

        //Primeiro resto da divisão por 11.
        resto = (d1 % 11);

        //Se o resultado for 0 ou 1 o digito é 0 caso contrário o digito é 11 menos o resultado anterior.
        if (resto < 2) {
            digito1 = 0;
        } else {
            digito1 = 11 - resto;
        }

        d2 += 2 * digito1;

        //Segundo resto da divisão por 11.
        resto = (d2 % 11);

        //Se o resultado for 0 ou 1 o digito é 0 caso contrário o digito é 11 menos o resultado anterior.
        if (resto < 2) {
            digito2 = 0;
        } else {
            digito2 = 11 - resto;
        }

        //Digito verificador do CPF que está sendo validado.
        String nDigVerific = cpf.substring(cpf.length() - 2, cpf.length());

        //Concatenando o primeiro resto com o segundo.
        nDigResult = String.valueOf(digito1) + String.valueOf(digito2);

        //comparar o digito verificador do cpf com o primeiro resto + o segundo resto.
        return nDigVerific.equals(nDigResult);
    }

    //retira a mascara do CPF e retorn os 11 numeros.
    public static String retiraMascaraCPF(String nomeMacara) {

        String nome = "";
        String aux = "";

        for (int nCount = 1; nCount < nomeMacara.length() + 1; nCount++) {
            aux = String.valueOf(nomeMacara.substring(nCount - 1, nCount));
            if (!(aux.equals("-")) && (!(aux.equals(".")))) {
                nome += aux;
            }
        }
        return nome;
    }

    //RETIRA A MASCARA DE UMA STRIND E RETORNA SEM MASCARA
    public static String retiraMascara(String nomeComMascara) {
        String nomeSemMascara = "";
        nomeSemMascara = nomeComMascara.replace(".", "").replace("-", "").replace("(", "").replace(")", "").replace(",", "").replace("[", "").replace("]", "").trim();
        return nomeSemMascara;
    }

    //Verifica se é um cnpj válido
    public static boolean isCnpj(String str_cnpj) {
        int soma = 0, dig;
        String cnpj_calc = str_cnpj.substring(0, 12);

        if (str_cnpj.length() != 14) {
            return false;
        }

        char[] chr_cnpj = str_cnpj.toCharArray();

        /* Primeira parte */
        for (int i = 0; i < 4; i++) {
            if (chr_cnpj[i] - 48 >= 0 && chr_cnpj[i] - 48 <= 9) {
                soma += (chr_cnpj[i] - 48) * (6 - (i + 1));
            }
        }
        for (int i = 0; i < 8; i++) {
            if (chr_cnpj[i + 4] - 48 >= 0 && chr_cnpj[i + 4] - 48 <= 9) {
                soma += (chr_cnpj[i + 4] - 48) * (10 - (i + 1));
            }
        }
        dig = 11 - (soma % 11);

        cnpj_calc += (dig == 10 || dig == 11)
                ? "0" : Integer.toString(dig);

        /* Segunda parte */
        soma = 0;
        for (int i = 0; i < 5; i++) {
            if (chr_cnpj[i] - 48 >= 0 && chr_cnpj[i] - 48 <= 9) {
                soma += (chr_cnpj[i] - 48) * (7 - (i + 1));
            }
        }
        for (int i = 0; i < 8; i++) {
            if (chr_cnpj[i + 5] - 48 >= 0 && chr_cnpj[i + 5] - 48 <= 9) {
                soma += (chr_cnpj[i + 5] - 48) * (10 - (i + 1));
            }
        }
        dig = 11 - (soma % 11);
        cnpj_calc += (dig == 10 || dig == 11)
                ? "0" : Integer.toString(dig);

        return str_cnpj.equals(cnpj_calc);
    }
}

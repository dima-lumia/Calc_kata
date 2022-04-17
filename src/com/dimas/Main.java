package com.dimas;

import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    static boolean onlyFrom0to10=true;  //Поддержка цифр от 0 до 10
    static boolean only2digit=true;  //поддержка только 2-х значных чисел

    enum RomArab { Arab(1),Rom(2),NoElse(0),Err(-1); //флаг сост. и типа вычислений: арабскими/ римскими /ни то ни се(ошибка)
        private int val;
        RomArab(int v) { this.val=v;       }
    }
    public static RomArab tipRA;

    enum Action{
        Plus,Minus,Div,Mult,Eqv,Err, Ravno;
    }
    public static Action action=null;

    public static void main(String[] args) throws IOException {

        System.out.println("Введите данные! Помните, работа с арабскими цифрами разрешена от 1 до 10!");

        Scanner sc=new Scanner(System.in);
        try {
            Pattern pattern=Pattern.compile("(?:([0-9]+)|([IVXLDMC]+))(\\s*[*/+-]\\s*)?+");
            while (sc.hasNext()) {
                int cntDig=0;
                String inputs=sc.nextLine();
                Matcher matcher=pattern.matcher(inputs);
                Expression exp=new Expression();
                tipRA=RomArab.NoElse;
                while (matcher.find()) {
                    int ival=-1;
                    if (matcher.start(1)>=0) {  //определитель арабских
                        String si=matcher.group(1);
                        if (tipRA ==RomArab.Rom) {
                            throw new NoValidateTipExeption("т.к. используются одновременно разные системы счисления:"+si);
                        }
                        tipRA=RomArab.Arab;
                        cntDig++;
                        ival=StringParser.toInt(si);
                        if (onlyFrom0to10 && (ival< 0 || ival>10)) throw new NoValidateTipExeption("Только от 0 до 10");
                        if (only2digit && (cntDig>2)) throw new NoValidateTipExeption("Только 2 числа");

                    }
                    if (matcher.start(2)>=0) { //определитель римских цифр
                        String si=matcher.group(2);
                        if (tipRA ==RomArab.Arab) {
                            throw new NoValidateTipExeption("т.к. используются одновременно разные системы счисления:"+si);
                        }
                        tipRA=RomArab.Rom;
                        ival=StringParser.toInt(si);
                        if (onlyFrom0to10 && (ival< 0 || ival>10)) throw new NoValidateTipExeption("Только от 0 до 10ти!");
                        if (only2digit && (cntDig>2)) throw new NoValidateTipExeption("Только 2 числа!");

                    }
                    if (matcher.start(3)>=0) {
                        String sz = matcher.group(3).trim();
                        action = StringParser.toAction(sz);
                    } else action=Action.Eqv;
                    System.out.print(tipRA.name()+"="); System.out.print(ival);System.out.println(","+action.name());
                    exp.calc(ival,action);

                }
                System.out.println("Результат="+exp.getiRez());

            }
        } catch (Exception | NoValidateTipExeption e) {
            System.out.println("т.к. строка не является математической операцией"+e.getMessage());
        }
        sc.close();

    }
}

package co.escuelaing.arep.service;

public class CalcService {

    public static String calc(String function,String number){
        try{
            switch(function){
                case "sin":
                    return '{'+"'number':"+Math.sin(Double.parseDouble(number))+'}';
                case "cos":
                    return '{'+"'number':"+Math.cos(Double.parseDouble(number))+'}';
                case "tan":
                    return '{'+"'number':"+Math.tan(Double.parseDouble(number))+'}';
                default:
                    return '{'+"'number':'Fallo'"+'}';
            }
        }catch(NumberFormatException e){
            return '{'+"'number':'Fallo'"+'}';
        }  
    }
}

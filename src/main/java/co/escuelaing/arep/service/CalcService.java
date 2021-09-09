package main.java.co.escuelaing.arep.service;

public class CalcService {

    public static String calc(String function,String number){
        System.err.println(function);
        System.out.println(number);
        try{
            switch(function){
                case "sin":
                    return '{'+"number:"+Math.sin(Double.parseDouble(inputLine)).toString()+'}';
                    break;
                case "cos":
                    return '{'+"number:"+Math.sin(Double.parseDouble(inputLine)).toString()+'}';
                    break;
                case "tan":
                    return '{'+"number:"+Math.sin(Double.parseDouble(inputLine)).toString()+'}';
                    break;
                default:
                    return '{'+"number:Fallo"+'}';
                    break;
            }
        }catch(NumberFormatException e){
            return '{'+"number:Fallo"+'}';
        }    
    }
}

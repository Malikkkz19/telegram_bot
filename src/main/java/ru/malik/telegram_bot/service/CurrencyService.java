package ru.malik.telegram_bot.service;

import javassist.Loader;
import org.json.JSONObject;
import org.jvnet.hk2.annotations.Service;
import ru.malik.telegram_bot.model.CurrencyModel;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

@Service
public class CurrencyService {
    public static String getCurrencyRate(String message, CurrencyModel currencyModel) throws IOException, ParseException {
        URL url = new URL("https://api.nbrb.by/exrates/rates/" + message + "?parammode=2");
        Scanner scanner = new Scanner((InputStream) url.getContent());
        String result = "";

        while(scanner.hasNext()) {
            result += scanner.nextLine();
        }

        JSONObject json = new JSONObject(result);

        currencyModel.setCur_ID(json.getInt("Cur_ID"));
        currencyModel.setDate(new SimpleDateFormat("dd/MM/yyyy'T'HH:mm:ss").parse(json.getString("Date")));
        currencyModel.setCur_Abbreviation(json.getString("Cur_Abbreviation"));
        currencyModel.setCur_Scale(json.getInt("Cur_Scale"));
        currencyModel.setCur_Name(json.getString("Cur_Name"));
        currencyModel.setCur_OfficialRate(json.getDouble("Cur_OfficialRate"));

        return "Official rate of BYN to" + currencyModel.getCur_Abbreviation() + "\n" + "on the date:" +
                getFormatDate(currencyModel) + "\n" +"is: " + currencyModel.getCur_OfficialRate() +
                "BYN per" + currencyModel.getCur_Scale() + " " + currencyModel.getCur_Abbreviation();
    }

    private static String getFormatDate(CurrencyModel currencyModel) {
        return new SimpleDateFormat("dd MM yyyy").format(currencyModel.getDate());
    }
}

package com.gmail.psyh2409;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.net.URL;

public class Parser {
    public static String getUrlForRequest(String url) throws IOException {
        url = "https://old.bank.gov.ua/control/uk/publish/article?art_id=38441973";
        return Jsoup.parse(new URL(url), 10000)
                        .selectFirst("div[class=main_block]")
                            .selectFirst("p[class=stan_link]")
                                .children()
                                    .first()
                                        .toString()
                                            .split("\"")[1];
    }
}

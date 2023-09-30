package com.app.dictionaryproject.service;

import com.microsoft.playwright.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CrawlData {
    private Page page;
    private Browser browser;
    private Playwright playwright;
    public Page routeAbort(Page page) {
        List<String> excluded_resource_types = new ArrayList<>(Arrays.asList("stylesheet", "script", "image", "font"));
        for (String types : excluded_resource_types) {
            page.route("**/*", route -> {
                if (types.equals(route.request().resourceType())){
                    route.abort();
                } else {
                    route.fallback();
                }
            });
        }
        return page;
    }

    public CrawlData() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(50));
        page = browser.newPage();
        page = routeAbort(page);
    }

    public String crawlPronounce(String eWord) {
        String text ="NONE";
        String word = eWord.replace(" ","-");
        if (word.contains("-")) {
            return text;
        }
        try {
            page.navigate("https://www.collinsdictionary.com/dictionary/english/"+word);
            text= page.waitForSelector("span.pron",new Page.WaitForSelectorOptions().setTimeout(200)).textContent();
        } catch (TimeoutError e) {
            try {
                page.navigate("https://www.collinsdictionary.com/dictionary/english/"+word);
                text=page.waitForSelector("span.pron",new Page.WaitForSelectorOptions().setTimeout(200)).textContent();
            } catch (TimeoutError ex) {
                System.out.println("Not found pronounce of : " + eWord);
            }
        }
        String pronounce = '/' + text.strip() + '/';
        return pronounce;
    }
}

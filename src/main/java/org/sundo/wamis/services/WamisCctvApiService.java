package org.sundo.wamis.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.sundo.wamis.entities.Observatory;
import org.sundo.wamis.repositories.ObservatoryRepository;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class WamisCctvApiService {
    private final RestTemplate restTemplate;
    private final ObservatoryRepository repository;

    /**
     * CCTV URL 업데이트
     *
     */
    public void updateCctvUrls() {
        List<Observatory> items = repository.findAll();
        String format  = "http://hrfco.go.kr/popup/cctvMainView.do?Obscd=%s";
        for (Observatory item : items) {
            try {
                String url = String.format(format, item.getObscd());
                String html = restTemplate.getForObject(url, String.class);
                if (!html.contains("var hurl") || !html.contains("var lurl")) {
                    continue;
                }


                Pattern pattern1 = Pattern.compile("var hurl[\\s]*=[\\s]*\"([^\"]*)\"");
                Pattern pattern2 = Pattern.compile("var lurl[\\s]*=[\\s]*\"([^\"]*)\"");

                Matcher matcher1 = pattern1.matcher(html);
                Matcher matcher2 = pattern2.matcher(html);

                if (matcher1.find()) {
                    String hUrl = matcher1.group(1);
                    item.setCctvUrlH(hUrl);
                }

                if (matcher2.find()) {
                    String lUrl = matcher2.group(1);
                    item.setCctvUrlL(lUrl);
                }

            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }

        repository.flush();
    }
}

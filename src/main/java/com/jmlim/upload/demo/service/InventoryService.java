package com.jmlim.upload.demo.service;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import com.jmlim.upload.demo.mapper.InventoryMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 375.987

@Slf4j
@Service
public class InventoryService {

    @Autowired
    private InventoryMapper inventoryMapper;

    @PostConstruct
    public void updateData() throws IOException {
        StopWatch watch = new StopWatch();
        watch.start();
        String fileName = "inventory";
        CsvReader products = new CsvReader("C:\\vt_data\\" + fileName + ".csv", ',', Charset.forName("MS949"));
        products.readHeaders();

        int i = 0;
        List<Map<String, Object>> paramList = new ArrayList<>();
        while (products.readRecord()) {
            Map<String, Object> params = new HashMap<>();
            String gsn = products.get("gsn");
            String usn = products.get("usn");
            String itemId = products.get("item_id");
            String gameItemId = products.get("game_item_id");

            params.put("gsn", gsn);
            params.put("usn", usn);
            params.put("itemId", itemId);
            params.put("gameItemId", gameItemId);
            params.put("itemType", 2);
            params.put("cdlId", "0");
            paramList.add(params);
            if (i % 2500 == 0) {
                System.out.println(i + " " + paramList.size() );

                log.debug("record no {}", params);
                inventoryMapper.insertInventory(paramList);
                paramList.clear();
            }
            i++;
        }
        inventoryMapper.insertInventory(paramList);
        System.out.println(i + " " + paramList.size() );
        watch.stop();

        System.out.println("total : " + watch.getTotalTimeMillis());
    }

    public void insertCsvData() throws IOException {
        CsvWriter writer = new CsvWriter("C:\\vt_data\\inventory.csv");

        //List<String[]> records = new ArrayList<String[]>();

        writer.writeRecord(new String[]{"gsn", "usn", "item_id", "game_item_id"});
        for (int i = 0; i < 500000; i++) {
            log.debug("record no {}", i);
            writer.writeRecord(new String[]{1 + "", 123 + "", i + "", i + ""});
        }
        writer.endRecord();
        writer.close();
    }
}

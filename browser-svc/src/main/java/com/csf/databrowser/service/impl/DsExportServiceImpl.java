package com.csf.databrowser.service.impl;

import com.csf.databrowser.dao.DsExportRecordDao;
import com.csf.databrowser.entity.DsExportRecord;
import com.csf.databrowser.service.DsExportSerivce;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Service
@Slf4j
public class DsExportServiceImpl implements DsExportSerivce,InitializingBean {

    private BlockingQueue<DsExportRecord> blockingQueue = new ArrayBlockingQueue<>(100);

    @Autowired
    private DsExportRecordDao dsExportRecordDao;

    @Override
    public void addExportData(DsExportRecord record) {
        log.info("export data={}", record.toString());
        blockingQueue.add(record);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Thread thread = new Thread(new ExportLogThread());
        thread.setDaemon(true);
        thread.setName("export_record");
        thread.start();
    }

    class ExportLogThread implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    DsExportRecord record = blockingQueue.take();
                    dsExportRecordDao.createExportRecord(record);
                } catch (Exception e) {
                   log.error("save export record error", e);
                }
            }
        }
    }
}

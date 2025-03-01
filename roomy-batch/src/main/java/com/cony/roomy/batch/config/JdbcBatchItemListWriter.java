package com.cony.roomy.batch.config;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.database.JdbcBatchItemWriter;

import java.util.List;

public class JdbcBatchItemListWriter<T> extends JdbcBatchItemWriter<List<T>> {

    private final JdbcBatchItemWriter<T> jdbcBatchItemWriter;

    public JdbcBatchItemListWriter(JdbcBatchItemWriter<T> jdbcBatchItemWriter) {
        this.jdbcBatchItemWriter = jdbcBatchItemWriter;
    }

    @Override
    public void write(Chunk<? extends List<T>> items) throws Exception {
        Chunk<T> totalList = new Chunk<>();

        for(List<T> list : items){
            totalList.addAll(list);
        }

        jdbcBatchItemWriter.write(totalList);
    }
}

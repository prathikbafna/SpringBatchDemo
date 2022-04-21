package com.springBatch.config;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.extensions.excel.RowMapper;
import org.springframework.batch.extensions.excel.mapping.BeanWrapperRowMapper;
import org.springframework.batch.extensions.excel.poi.PoiItemReader;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.springBatch.entity.Product;
import com.springBatch.listener.MyJobListener;
import com.springBatch.processor.ProductProcessor;

@Configuration
@EnableBatchProcessing
public class BatchConfig {



	//reader class object
//	@Bean
//	public FlatFileItemReader<Product> reader(){
//		FlatFileItemReader<Product> reader = new FlatFileItemReader<Product>();
//		reader.setResource(new ClassPathResource("products.csv")); //src/main/resources
////		reader.setResource(new FileSystemResource("D:/sample/products.csv"));
////		reader.setResource(new UrlResource("http://abcd.com/products.csv"));
//		
//		reader.setLineMapper(new DefaultLineMapper<>() {{
//			setLineTokenizer(new DelimitedLineTokenizer() {{
//				setDelimiter(",");
//				setNames("productId","productName","productCost");
//			}});
//		
//			setFieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
//				setTargetType(Product.class);
//				}});
//			}});
//			
//		return reader;
//	}
	
	
	@Bean
    public PoiItemReader<Product> reader() {
        PoiItemReader<Product> reader = new PoiItemReader<>();
        reader.setLinesToSkip(0);
        reader.setResource(new ClassPathResource("fumm.xlsx"));
        reader.setRowMapper(excelRowMapper());
        return reader;
    }
 
    private RowMapper<Product> excelRowMapper() {
        BeanWrapperRowMapper<Product> rowMapper = new BeanWrapperRowMapper<>();
        rowMapper.setTargetType(Product.class);
        return rowMapper;
    }
	
	//processor class object
	@Bean
	public ItemProcessor<Product, Product> processor(){
		return new ProductProcessor();
	}
	
	
//	@Autowired
//	private DataSource dataSource;
	
	//writer class object
	@Bean
	public ItemWriter<Product> writer(){
		ItemWriter<Product> writer = new ItemWriter<Product>() {

			@Override
			public void write(List<? extends Product> items) throws Exception {
				// TODO Auto-generated method stub
				
			}
		};
		
		return writer;
	}
	
	//Listener class object
	@Bean
	public JobExecutionListener listener() {
		return new MyJobListener();
	}
	
	//autowire step Builder factory
	@Autowired
	private StepBuilderFactory sf;
	
	//step object
	@Bean
	public Step stepA() {
		
		return sf.get("stepA")
				.<Product,Product>chunk(3)
				.reader(reader())
				.writer(writer())
				.processor(processor())
				.build();
	}
	
	
	//autowire job builder factory
	@Autowired
	private JobBuilderFactory jf;
	
	
	//job object
	@Bean
	public Job jobA() {
		return jf.get("jobA")
				.incrementer(new RunIdIncrementer())
				.listener(listener())
				.start(stepA())
				//.next(stepB())
				//.next(stepC())
				.build();
	}
	
	
	
	
}

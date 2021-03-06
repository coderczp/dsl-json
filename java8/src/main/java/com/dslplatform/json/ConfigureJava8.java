package com.dslplatform.json;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;

public class ConfigureJava8 implements Configuration {
	@Override
	public void configure(DslJson json) {
		json.registerReader(LocalDate.class, JavaTimeConverter.LocalDateReader);
		json.registerWriter(LocalDate.class, JavaTimeConverter.LocalDateWriter);
		json.registerReader(LocalDateTime.class, JavaTimeConverter.LocalDateTimeReader);
		json.registerWriter(LocalDateTime.class, JavaTimeConverter.LocalDateTimeWriter);
		json.registerReader(OffsetDateTime.class, JavaTimeConverter.DateTimeReader);
		json.registerWriter(OffsetDateTime.class, JavaTimeConverter.DateTimeWriter);
		json.registerReader(java.sql.Date.class, rdr -> java.sql.Date.valueOf(JavaTimeConverter.deserializeLocalDate(rdr)));
		json.registerWriter(java.sql.Date.class, new JsonWriter.WriteObject<java.sql.Date>() {
			@Override
			public void write(JsonWriter writer, java.sql.Date value) {
				JavaTimeConverter.serialize(value.toLocalDate(), writer);
			}
		});
		json.registerReader(java.sql.Timestamp.class, rdr -> java.sql.Timestamp.from(JavaTimeConverter.deserializeDateTime(rdr).toInstant()));
		json.registerWriter(java.sql.Timestamp.class, new JsonWriter.WriteObject<java.sql.Timestamp>() {
			@Override
			public void write(JsonWriter writer, java.sql.Timestamp value) {
				JavaTimeConverter.serialize(OffsetDateTime.ofInstant(value.toInstant(), ZoneId.systemDefault()), writer);
			}
		});
		json.registerReader(java.util.Date.class, rdr -> java.util.Date.from(JavaTimeConverter.deserializeDateTime(rdr).toInstant()));
		json.registerWriter(java.util.Date.class, new JsonWriter.WriteObject<java.util.Date>() {
			@Override
			public void write(JsonWriter writer, java.util.Date value) {
				JavaTimeConverter.serialize(OffsetDateTime.ofInstant(value.toInstant(), ZoneId.systemDefault()), writer);
			}
		});
		json.registerWriter(ResultSet.class, ResultSetConverter.Writer);
	}
}

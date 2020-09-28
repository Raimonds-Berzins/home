package lv.maska;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import spark.ResponseTransformer;

/**
 * jsonMapper
 */
public class JsonMapper implements ResponseTransformer {

    private static ObjectMapper mapper = new ObjectMapper()
            .configure(SerializationFeature.INDENT_OUTPUT, true);

    public static ObjectMapper get(){
        return mapper;
    }

	@Override
	public String render(Object model) throws Exception {
		return mapper.writeValueAsString(model);
	}
    
}
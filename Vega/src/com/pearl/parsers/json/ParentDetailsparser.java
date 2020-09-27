package com.pearl.parsers.json;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;

import com.pearl.parent.ParentsList;
import com.pearl.ui.models.Parent;

// TODO: Auto-generated Javadoc
/**
 * The Class ParentDetailsparser.
 */
public class ParentDetailsparser {



    /**
     * Gets the parent details.
     *
     * @param content the content
     * @return the parent details
     */
    public static Parent getParentDetails(String content) {

	Parent parent=null;
	final ObjectMapper objMapper=new ObjectMapper();

	try {
	    parent=objMapper.readValue(content,Parent.class);
	} catch (final JsonParseException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (final JsonMappingException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (final IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	return parent;

    }


    /**
     * Gets the parents list.
     *
     * @param content the content
     * @return the parents list
     */
    public static List<Parent> getParentsList(String content){

	ParentsList parentsList=null;
	final ObjectMapper objMapper=new ObjectMapper();
	final ObjectReader objReader=objMapper.reader(ParentsList.class);
	content="{\"_parentsList\":"+content+"}";
	try {
	    parentsList=objReader.readValue(content);
	} catch (final JsonProcessingException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (final IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	return parentsList.get_parentsList();
    }


}

/**
 * 
 */
package br.com.jvoliveira.sourceminer.component.commitparser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.jvoliveira.sourceminer.domain.Feature;
import br.com.jvoliveira.sourceminer.domain.RepositoryRevision;
import br.com.jvoliveira.sourceminer.domain.Task;

/**
 * @author João Victor
 *
 */
public class SimpleCommitParser implements CommitParser{

	@Override
	public Collection<Task> extractTaskFromMessage(RepositoryRevision revision) {
		String message = revision.getComment().trim();
		String pattern = "#\\d+\\s";
		
		Pattern p = Pattern.compile(pattern);
	    Matcher m = p.matcher(message);
	    Collection<Task> result = new ArrayList<>();
	    while(m.find()){
	    	String taskNumber = message.substring(m.start()+1, m.end()).trim();
	    	result.add(new Task(taskNumber,revision));
	    }
		
		return result;
	}

	@Override
	public Collection<Feature> extractFeatureFromMessage(RepositoryRevision revision) {
		String message = revision.getComment().trim();
		String pattern = "@[\\w+\\/?]*";
		
		Pattern p = Pattern.compile(pattern);
	    Matcher m = p.matcher(message);
	    Collection<Feature> result = new ArrayList<>();
	    while(m.find()){
	    	String featurePath = message.substring(m.start()+1, m.end()).trim();
	    	result.add(new Feature(featurePath));
	    }
		
		return result;
	}

	
	
}

/**
 * 
 */
package br.com.jvoliveira.sourceminer.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jvoliveira.arq.service.AbstractArqService;
import br.com.jvoliveira.sourceminer.component.commitparser.CommitParser;
import br.com.jvoliveira.sourceminer.component.commitparser.SimpleCommitParser;
import br.com.jvoliveira.sourceminer.domain.Feature;
import br.com.jvoliveira.sourceminer.domain.RepositoryRevision;
import br.com.jvoliveira.sourceminer.domain.Task;
import br.com.jvoliveira.sourceminer.repository.FeatureRepository;
import br.com.jvoliveira.sourceminer.repository.TaskRepository;

/**
 * @author João Victor
 *
 */
@Service
public class TaskService extends AbstractArqService<Task>{

	private CommitParser commitParser;
	private FeatureRepository featureRepository;
	
	@Autowired
	public TaskService(TaskRepository repository, FeatureRepository featureRepository){
		this.repository = repository;
		this.featureRepository = featureRepository;
		commitParser = new SimpleCommitParser();
	}
	
	public Collection<Task> createTaskFromRevision(RepositoryRevision revision){
		Collection<Task> transientTasks = commitParser.extractTaskFromMessage(revision);
		Collection<Feature> transientFeatures = commitParser.extractFeatureFromMessage(revision);
		
		if(transientTasks == null || transientTasks.isEmpty())
			return Collections.emptyList();
		
		Collection<Task> tasks = fetchTasks(transientTasks);
		Collection<Feature> features = fetchFeature(transientFeatures);
		
		for(Task task : tasks){
			task.getRevisions().add(revision);
			task.getFeatures().addAll(features);
			task.setCreateAt(Calendar.getInstance().getTime());
			getRepository().save(task);
			revision.setTask(task);
			getDAO().update(revision);
		}
		
		return tasks;
	}
	
	private Collection<Task> fetchTasks(Collection<Task> tasks){
		Iterator<Task> iterator = tasks.iterator();
		Collection<Task> result = new ArrayList<>();
		
		while(iterator.hasNext()){
			Task task = iterator.next();
			Collection<Task> tasksFromDB = getRepository().findByNumberAndProject(task.getNumber(), task.getProject());
			if(tasksFromDB == null || tasksFromDB.isEmpty())
				result.add(task);
			else
				result.add(tasksFromDB.iterator().next());
		}
		
		return result;
	}
	
	private Collection<Feature> fetchFeature(Collection<Feature> features){
		Iterator<Feature> iterator = features.iterator();
		Collection<Feature> result = new ArrayList<>();
		
		while(iterator.hasNext()){
			Feature feature = iterator.next();
			Collection<Feature> featuresFromDB = featureRepository.findByPathAndProject(feature.getPath(), feature.getProject());
			if(featuresFromDB == null || featuresFromDB.isEmpty())
				result.add(feature);
			else
				result.add(featuresFromDB.iterator().next());
		}
		
		return features;
	}
	
	private TaskRepository getRepository(){
		return (TaskRepository) repository;
	}
}

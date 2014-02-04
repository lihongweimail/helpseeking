package cn.edu.fudan.se.helpseeking.test.googleAPI;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.core.search.SearchParticipant;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.core.search.SearchRequestor;


public class SearchJavaElements {

//	JDT Java Search Engine provides a function to quick search Java projects in the workspace for Java elements, such as method references, field declarations, implementors of an interface, etc.
//
//	There are mainly 4 steps involved with a search:
//
//	Create a search pattern
//	Create a search scope
//	Define result collector
//	Start Search
//	The example below follows those steps. There are more options for each of those steps, see the reference to find out more flexibilities.
	
	public Object execute(ExecutionEvent event) throws ExecutionException {
		 
		// step 1: Create a search pattern
		// search methods having "abcde" as name
		SearchPattern pattern = SearchPattern.createPattern("abcde",IJavaSearchConstants.METHOD, IJavaSearchConstants.DECLARATIONS,	SearchPattern.R_EXACT_MATCH);
 
		// step 2: Create search scope
		// IJavaSearchScope scope = SearchEngine.createJavaSearchScope(packages);
		IJavaSearchScope scope = SearchEngine.createWorkspaceScope();
 
		// step3: define a result collector
		SearchRequestor requestor = new SearchRequestor() {
			public void acceptSearchMatch(SearchMatch match) {
				System.out.println(match.getElement());
			}
		};
 
		// step4: start searching
		SearchEngine searchEngine = new SearchEngine();
		try {
           SearchParticipant[] searchParticipants=new SearchParticipant[] { SearchEngine.getDefaultSearchParticipant() };
           IProgressMonitor monitor=null;
           	searchEngine	.search(pattern, searchParticipants, scope, requestor,monitor ); //null /* progress monitor is not used here */
		} catch (CoreException e) {
			e.printStackTrace();
		}
 
		return null;
	}
	
	
	public Object execute2(ExecutionEvent event) throws ExecutionException {
		 
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject[] projects = root.getProjects();
 
		for (IProject project : projects) {
			IJavaProject javaProject = JavaCore.create(project);
 
			IPackageFragment[] packages = null;
			try {
 
				packages = javaProject.getPackageFragments();
 
			} catch (JavaModelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
 
			// step 1: Create a search pattern
			// search
			SearchPattern pattern = SearchPattern.createPattern("abcde",
					IJavaSearchConstants.METHOD,
					IJavaSearchConstants.DECLARATIONS,
					SearchPattern.R_EXACT_MATCH);
 
			// step 2: Create search scope
			 IJavaSearchScope scope = SearchEngine.createJavaSearchScope(packages);
 
			// step3: define a result collector
			SearchRequestor requestor = new SearchRequestor() {
				public void acceptSearchMatch(SearchMatch match) {
					// System.out.println(&quot;good&quot;);
					System.out.println(match.getElement());
					// System.out.println(match.getElement().toString());
				}
			};
 
			// step4: start searching
			SearchEngine searchEngine = new SearchEngine();
			try {
				searchEngine.search(pattern,
						new SearchParticipant[] { SearchEngine
								.getDefaultSearchParticipant() }, scope,
						requestor, null /* progress monitor */);
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				System.out.println("exception");
				e.printStackTrace();
			}
 
		}
 
		return null;
	}
	
	
	}
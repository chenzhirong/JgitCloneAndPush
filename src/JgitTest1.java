import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jgit.api.CreateBranchCommand.SetupUpstreamMode;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.CanceledException;
import org.eclipse.jgit.api.errors.ConcurrentRefUpdateException;
import org.eclipse.jgit.api.errors.DetachedHeadException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidConfigurationException;
import org.eclipse.jgit.api.errors.InvalidRefNameException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.api.errors.NoFilepatternException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.NoMessageException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.api.errors.UnmergedPathsException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffEntry.ChangeType;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilterGroup;
import org.junit.Before;
import org.junit.Test;

public class JgitTest1 {

	private String localPath, remotePath;
	private Repository repository;
	private Git git;
	private FileRepositoryBuilder builder;

	@Before
	public void init() throws IOException {
		localPath = "jonee/config";
		remotePath = "https://github.com/chenzhirong/rcp_view.git";
		//localRepo = new FileRepository(localPath + "/.git");
		//git = new Git(localRepo);
	}

	// @Test
	public void testCreate() throws IOException {
		Repository newRepo = new FileRepository(localPath + "/.git");
		newRepo.create();
	}

	// ¿ËÂ¡¿â
	//@Test
	public void testClone() throws IOException, InvalidRemoteException,
			TransportException, GitAPIException {
		builder= new FileRepositoryBuilder();
		File f = new File(localPath);
		 repository = builder.setGitDir(f).readEnvironment()
				.findGitDir().build();
		 git = new Git(repository);
		CloneCommand clone = git.cloneRepository();
		clone.setBare(false);
		clone.setCloneAllBranches(true);
		clone.setDirectory(f).setURI(remotePath);
		UsernamePasswordCredentialsProvider user = new UsernamePasswordCredentialsProvider(
				"chenzhirong", "rongzai521");
		clone.setCredentialsProvider(user);
		//clone.call();
	}

	@Test
	public void testAdd() throws IOException, GitAPIException {
		File f = new File(localPath+"/.git");
		builder= new FileRepositoryBuilder();
		File myfile = new File(localPath+"/rcp_view/src/jonee/view/" + System.currentTimeMillis() + ".java");
		FileWriter fileWriter = new FileWriter(myfile);
		fileWriter.write(System.currentTimeMillis() + "rongzai");
		fileWriter.flush();
		fileWriter.close();
		myfile.createNewFile();
		 repository = builder.setGitDir(f).readEnvironment()
					.findGitDir().build();
			 git = new Git(repository);
		git.add().addFilepattern(localPath).call();
		git.commit().setCommitter("chenzhirong", "253494709@qq.com")
				.setMessage("jonee commit").call();

		// ÈÕ¼Ç
		for (RevCommit revCommit : git.log().call()) {
			System.out.println(revCommit);
			System.out.println(revCommit.getFullMessage());
			System.out.println(revCommit.getCommitterIdent().getName()
					+ "======="
					+ revCommit.getCommitterIdent().getEmailAddress());
		}
	}

	@Test
	public void testCommit() throws IOException, JGitInternalException,
			UnmergedPathsException, GitAPIException {
		RepositoryBuilder builder=new RepositoryBuilder();
		File f = new File(localPath+"/.git");
		Repository repository=builder.setGitDir(f).readEnvironment().findGitDir().build();
		
		Git git = new Git(repository);
		RevWalk walk = new RevWalk(repository);
		RevCommit commit = null;
		Iterable<RevCommit> logs = git.log().call();
		Iterator<RevCommit> i = logs.iterator();

		while (i.hasNext()) {
		    commit = walk.parseCommit( i.next() );
		    System.out.println( commit.getFullMessage() );
		}
	}

	@Test
	public void testPush() throws IOException, JGitInternalException,
			TransportException, GitAPIException {
		try {
		      Repository localRepo = new FileRepository("https://github.com/chenzhirong/rcp_view.git");
		      localRepo.create();
		      testAdd();
		      Git git=new Git(localRepo);
		      git.push().call();
		      localRepo.close();
		} catch (IllegalStateException ise) {
		        System.out.println("The repository already exists!");
		} catch (IOException ioe) {
		        System.out.println("Failed to create the repository!");
		} catch (NoFilepatternException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		} catch (GitAPIException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	}

	//@Test
	public void testTrackMaster() throws IOException, JGitInternalException,
			GitAPIException {
		git.branchCreate().setName("master")
				.setUpstreamMode(SetupUpstreamMode.SET_UPSTREAM)
				.setStartPoint("origin/master").setForce(true).call();
	}

	//@Test
	public void testPull() throws IOException, TransportException,
			GitAPIException {
		git.pull().call();
	}
}
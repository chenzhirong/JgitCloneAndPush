package jgit;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.CreateBranchCommand.SetupUpstreamMode;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
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
import org.eclipse.jgit.transport.RemoteRefUpdate;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilterGroup;
import org.junit.Before;
import org.junit.Test;

import com.sun.jndi.rmi.registry.RemoteReference;

public class PushTest {

	private static String localPath = "F:/data";
	private static String remotePath = "https://github.com/chenzhirong/data.git";
	private static  Repository repository;
	private static  Git git;
	private static FileRepositoryBuilder builder;
	private static File f;

	// @Test
	public void testCreate() throws IOException {
		Repository newRepo = new FileRepository(localPath + "/.git");
		newRepo.create();
	}

	// 克隆库
//	 @Test
	public static void testClone() throws IOException, InvalidRemoteException,
			TransportException, GitAPIException {
		
		builder = new FileRepositoryBuilder();
		f = new File(localPath);//克隆不要/.git
		repository = builder.setGitDir(f).readEnvironment().findGitDir()
				.build();
		git = new Git(repository);
		CloneCommand clone = git.cloneRepository();
		clone.setBare(false);
		clone.setCloneAllBranches(true);
		clone.setDirectory(f).setURI(remotePath);
		UsernamePasswordCredentialsProvider user = new UsernamePasswordCredentialsProvider(
				"chenzhirong", "rongzai521");
		clone.setCredentialsProvider(user);
		if(!f.isDirectory()){
			clone.call();
			System.out.println("克隆");
		}
		
	}
	@Test
	public static void testPush() throws IOException, JGitInternalException,
			TransportException, GitAPIException {
		File f = new File(localPath+"/.git");
		builder= new FileRepositoryBuilder();
		Date date = new Date(); 
		//format对象是用来以指定的时间格式格式化时间的 
		SimpleDateFormat from = new SimpleDateFormat( 
		"yyyy年MM月dd日HH.mm.ss"); //这里的格式可以自己设置 
		//format()方法是用来格式化时间的方法 
		String times = from.format(date); 
		System.out.println(times); 
		File myfile = new File(localPath+"/" + times + ".java");
		FileWriter fileWriter = new FileWriter(myfile);
		fileWriter.write(times + "rongzai");
		fileWriter.flush();
		fileWriter.close();
		myfile.createNewFile();
		 repository = builder.setGitDir(f).readEnvironment()
					.findGitDir().build();
			Git git1 = new Git(repository);
			git1.add().addFilepattern(".").call();
			git1.commit().setCommitter("chenzhirong", "253494709@qq.com")
				.setMessage("jonee commit").call();
			PushCommand push=git1.push();
			push.setRemote(remotePath);
			UsernamePasswordCredentialsProvider user = new UsernamePasswordCredentialsProvider("chenzhirong","rongzai521");
			push.setCredentialsProvider(user);
			push.call();
	    //repository.close();

	}
	public static void main(String[] args) {
		PushTest test=new PushTest();
		try {
			test.testClone();
			test.testPush();
		} catch (InvalidRemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GitAPIException e) {
			e.printStackTrace();
		}
	}

}
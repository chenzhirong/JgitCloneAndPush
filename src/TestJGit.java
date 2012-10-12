import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.CreateBranchCommand.SetupUpstreamMode;
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
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepository;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.junit.Before;
import org.junit.Test;

public class TestJGit {

    private String localPath, remotePath;
    private Repository localRepo;
    private Git git;

    @Before
    public void init() throws IOException {
        localPath = "home/me/repos/mytest";
        localRepo = new FileRepository(localPath + "/.git");
        git = new Git(localRepo);        
    }
 

    @Test
    public void testCreate() throws IOException {
        Repository newRepo = new FileRepository(localPath + ".git");
        newRepo.create();
    }

    @Test    
    public void testClone() throws IOException, InvalidRemoteException, TransportException, GitAPIException {     
        Git.cloneRepository() 
           .setURI(remotePath)
           .setDirectory(new File(localPath))
           .call();  
    }

    @Test
    public void testAdd() throws IOException, GitAPIException { 
        File myfile = new File(localPath + "/myfile");
        myfile.createNewFile();
        git.add()
           .addFilepattern("myfile")
           .call();
        
    }

    @Test
    public void testCommit() throws IOException, JGitInternalException, UnmergedPathsException, GitAPIException {
        git.commit()
           .setMessage("Added myfile")
           .call();
    }

    @Test
    public void testPush() throws IOException, JGitInternalException, TransportException, GitAPIException {     
        git.push()
           .call();
    }    

   // @Test
    public void testTrackMaster() throws IOException, JGitInternalException, GitAPIException {     
        git.branchCreate() 
           .setName("master")
           .setUpstreamMode(SetupUpstreamMode.SET_UPSTREAM)
           .setStartPoint("origin/master")
           .setForce(true)
           .call();
    }

   //@Test
    public void testPull() throws IOException, TransportException, GitAPIException {
        git.pull()
           .call();
    }
}
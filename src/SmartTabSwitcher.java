import com.intellij.ide.actions.ShowRecentlyEditedFilesAction;
import com.intellij.ide.actions.Switcher;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileEditor.ex.IdeDocumentHistory;
import com.intellij.openapi.fileEditor.impl.EditorWindow;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.tabs.JBTabs;
import org.apache.log4j.Level;

public class SmartTabSwitcher extends ShowRecentlyEditedFilesAction {
    private final static Logger logger = LoggerFactory.createLogger();

    @Override
    public void actionPerformed(AnActionEvent e) {
        logger.setLevel(Level.ALL);
        logger.info("asd");
        
        Project project = e.getProject();
        if(project == null) 
        {
            logger.debug("No project");
            return;
        }

        // TODO(hbt) NEXT add filter for same window only -- new command
        // TODO(hbt) NEXT get all windows
        
            EditorWindow editorWindow = EditorWindow.DATA_KEY.getData(e.getDataContext());
            JBTabs tabs = editorWindow.getTabbedPane().getTabs();
//            tabs.findInfo()
            VirtualFile[] files = editorWindow.getFiles();
            VirtualFile[] tabFiles = new VirtualFile[tabs.getTabCount()];
            for (VirtualFile f : files) {
                if (tabs.findInfo(f) != null) {
                    logger.debug(tabs.findInfo(f).getText());
                    tabFiles[tabs.getIndexOf(tabs.findInfo(f))] = f;
                }
            }


            logger.debug("COUNT" + tabFiles.length);
            Switcher.SwitcherPanel recently_edited_files = Switcher.createAndShowSwitcher(e, "Current Tabs", true, tabFiles);

        // TODO(hbt) NEXT rm
//        if (project != null) {
//            Switcher.SwitcherPanel current_tabs = Switcher.createAndShowSwitcher(e, "Current Tabs", true, IdeDocumentHistory.getInstance(project).getChangedFiles());
//        }
        
    }
}

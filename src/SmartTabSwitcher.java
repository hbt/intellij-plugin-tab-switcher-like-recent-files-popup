import com.intellij.ide.actions.Switcher;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileEditor.ex.IdeDocumentHistory;
import com.intellij.openapi.project.Project;

public class SmartTabSwitcher extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        if (project != null) {
            Switcher.SwitcherPanel current_tabs = Switcher.createAndShowSwitcher(e, "Current Tabs", true, IdeDocumentHistory.getInstance(project).getChangedFiles());
        }
    }
}

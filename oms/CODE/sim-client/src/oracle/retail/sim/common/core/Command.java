package oracle.retail.sim.common.core;

public abstract class Command {
  public final void execute() throws Exception {
    doExecute();
  }
  
  protected abstract void doExecute() throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\core\Command.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
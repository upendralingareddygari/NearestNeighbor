import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;



/**
 * 
 * @author upendra
 *
 */
public class CVNearestNeighbor {

	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		if(args.length != 2 || args[0].isEmpty() || args[1].isEmpty()) {
			System.out.println("Please pass the file locations correctly");
			return;
		}
		
		BusinessLogic businessLogic = new BusinessLogic();
		businessLogic.setUpCrossValidationStuff(args[0], args[1]);
		businessLogic.startValidation();
				
	}
}
 

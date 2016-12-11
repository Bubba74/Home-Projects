package main;

public class Network {

	/*
	 * Henry Loh
	 * Neural Network for classifying languages
	 * Does not really work
	 * To run with graphics, run NetworkGraphics instead.
	 * 
	 */
	
	static int[] inputs = new int[16*26];
	static float[] mid = new float[16];
	static float[][] synapses = new float[2][];
	static float[] outputs = new float[12];
	static String[] outputNames = {"Random","English","Chinese","German","Italian","Spanish","Latin","Dutch","French","Portuguese","Gailic","Icelandic"};
	
	static int timesToTrain = 100;
	static String testWord = "NA";
	static float outputSum = 1;
	static int prediction = 0;
	static float step = 0.001f;

	static boolean[] successes = new boolean[200];
	static int successesIndex = 0;
	
	public static void refresh (){
		for (int i=0;i<inputs.length;i++) inputs[i] = 0;
		for (int i=0;i<mid.length;i++) mid[i] = 0;
		for (int i=0;i<outputs.length;i++) outputs[i] = 0;
	}//refresh method
	public static void loadInputs(String s){
		//Load Inputs
		for (int i=0;i<s.length();i++){
			int c = (int)s.charAt(i);
			if ('A' <= c && c <= 'Z') c += 32;
			int index = c-97;
			inputs[i*26+index] = 1;
		}
	}//loadInputs method
	public static void travelSynapse(int synapseNum){
		switch(synapseNum){
		case 1:
			for (int i=0;i<inputs.length;i++){
				if (inputs[i] == 0) continue;
				for (int j=0;j<16;j++)
					mid[j] += inputs[i]*synapses[0][i*16+j];
			}
			break;
		case 2:
			for (int i=0;i<mid.length;i++)
				for (int j=0;j<12;j++)
					outputs[j] += mid[i]*synapses[1][i*12+j];
			break;
		default:
			System.out.println("Synapse Number "+synapseNum+" does not exist!!");
			System.exit(1);
		}
	}//travelSynapse method
	public static void normalize(){
		//Turn output layer into percentages
		float sum = 0;
		for (int i=0;i<12;i++) sum += outputs[i];
		sum = 1/sum;
		for (int i=0;i<12;i++) outputs[i] *= sum;
		outputSum = 1/sum;
	}//normalize method
				
	public static int maxIndex(float [] outputs){
		int max = 0;
		for (int i=0;i<outputs.length;i++){
			if (outputs[i] > outputs[max]){
				max = i;
			}
		}
		return max;
	}//maxIndex method
	
	public static String successRate(){
		int correct = 0;
		for (int i=0;i<successes.length;i++) if (successes[i]) correct++;
		return String.format("%.2f",(float)correct/successes.length);
	}//successRate method
	
	public static void test(String s, boolean verb){

		if (verb) System.out.println("Testing string "+s);

		if (s.length() > 16) {
			System.out.println("ERROR, TOO LONG");
			return;
		}
		refresh();
		
		testWord = s;
		
		loadInputs(s);
		travelSynapse(1);
		travelSynapse(2);
		
		normalize();
		prediction = maxIndex(outputs);
		
		if (verb) for (int i=0;i<12;i++) System.out.printf("%11s -- %.2f%%\n",outputNames[i],100*outputs[i]);

	}//test input
	
	public static void test(String s) {
		test(s, false);
	}//test1 method

	public static boolean trainSynapse1(int i/*Synapse Index*/, int ans, float dVal){
		
		int midIndex = i%16;
		
		mid[midIndex] += dVal;/* inputs[i/16] Must equal 1*/
		
		//Travel synapse 2 for the updated middle value;
		int s2Start = 12*midIndex;
		int s2Step = 0;
		//Check improvements (Ex: outputs[ans] might be 0.08
		float[] feauxOutputs = new float[12];
		while (s2Step < 12){
			//Add to output ..deltaMid * synapse2;
			feauxOutputs[s2Step] = dVal*synapses[1][s2Start+s2Step];
			s2Step++;
		}
		//Normalize FeauxOutputs
		float sum = 0;
		for (float out: feauxOutputs) sum += out;
		sum = 1 / sum;
		for (int o=0;o<12;o++) feauxOutputs[o] *= sum;
		
		//If output improved
		if (feauxOutputs[ans] > outputs[ans]){
			//Commit to change
			synapses[0][i] += dVal;
			
			//Already incremented mid value
			
			//Update outputs
			for (int o=0;o<12;o++) outputs[o] *= outputSum;
			for (int o=0;o<12;o++) outputs[o] += dVal*synapses[1][12*midIndex+o];
			normalize();
			
			//Don't check a downward movement
			return true;
		} else {
			mid[midIndex] -= dVal;
			return false;
		}
	}//trainSynapse1 method
	public static boolean trainSynapse2(int i/*Synapse Index*/, int ans, float dVal){
		
		int endIndex = i%12;
		
		//If synapse leads to ans output
		if ((dVal > 0 && endIndex == ans) || (dVal < 0 && endIndex != ans)){
			//Commit to change
			synapses[1][i] += dVal;
			
			//Already incremented mid value
			
			//Update outputs
			for (int o=0;o<12;o++) outputs[o] *= outputSum;
			outputs[endIndex] += dVal*mid[i/16];
			normalize();
//			travelSynapse(2);
//			normalize();
			
			//Don't check a downward movement
			return true;
		} else {
			return false;
		}
		
	}//trainSynapse2 method
	
	public static void train(String s, int ans){
		//Load input into system
		test(s,false);
		if (successesIndex == successes.length) successesIndex = 0;
		successes[successesIndex] = prediction == ans;
		successesIndex++;
		//Synapse 1
		for (int i=0;i<synapses[0].length;i++){
			//test(s,false);
		
			//No input, synapses have no effect.
			if (inputs[i/(16)] == 0) {/*System.out.println(i);*/continue;}
//			System.out.println(i/(16));
			
			boolean didInc = false;

			//Check train increase
			if (synapses[0][i] <= 1-step)
				didInc = trainSynapse1(i,ans,step);
			
//			//Check train decrease
			if (!didInc && synapses[0][i] >= step)
				trainSynapse1(i,ans,-step);

		}//1
		
		//Synapse 2
		for (int i=0;i<synapses[1].length;i++){
			
			boolean didInc = false;
			
			//Check train increase
			if (synapses[1][i] <= 1-step)
				didInc = trainSynapse2(i,ans,step);
			
			//Check train decrease
			if (!didInc && synapses[1][i] >= step){
				trainSynapse2(i,ans,-step);
			}
			
		}//2
		
	}//train method
	
	public static void init(){
		//Declare Synapses
		synapses[0] = new float[16*26*16];
		synapses[1] = new float[16*12];

		//Fill Synapses With Random Values
		for (int i=0;i<synapses[0].length;i++) synapses[0][i] = (float) Math.random();
		for (int i=0;i<synapses[1].length;i++) synapses[1][i] = (float) Math.random();
		
	}//init method
	
	public static void main(String[] args){
		//Start Time Marker
		long start = System.currentTimeMillis();

		init();
		
		//Create a Copy of Initial Synapse Values
		float[][] first_synapses = {synapses[0].clone(),synapses[1].clone()};

		//First Test
		test("Revolutionary",true);

///////////////////////TRAINING/////////////////////ROOM//////////////////////////////////

		LangDict english = new LangDict("English");
		LangDict dutch = new LangDict("Dutch");
		
		int len = english.count;
		if (dutch.count < len) len = dutch.count;
		
		System.out.println("Training Count: "+timesToTrain);
		System.out.printf("English: %d words.\nDutch: %d words.\n",english.count,dutch.count);
		//Time Marker
		float trainingStartTime = System.currentTimeMillis();

		for (int trainingCounts=0;trainingCounts<timesToTrain;trainingCounts++){
			System.out.println("Training Round "+(trainingCounts+1));
			for (int i=0;i<len;i++){
				train(english.getWord(i),1);
				train(dutch.getWord(i),7);
			}
		}

		System.out.printf("Training finished in %.4f seconds.\n",((float)System.currentTimeMillis()-trainingStartTime)/1000);
//////////////////////////////////////////////////////////////////////////////////////////
		//Test Progress
		test("Revolutionary",true);
		test("Market",true);
		test("Shopping",true);
		test("zijn",true);
		
		//End Time Marker
		long end = System.currentTimeMillis();
		System.out.println("Time Elapsed: "+(end-start));
		
		processArrayChanges("Synapse1",first_synapses[0],synapses[0]);
		processArrayChanges("Synapse2",first_synapses[1],synapses[1]);
		
	}//main method

	public static void reset(){
		refresh();
		init();
		successes = new boolean[200];
		
	}//reset method
	
	public static void processArrayChanges(String name, float[] src, float[] dest){
		if (src.length != dest.length){
			System.out.println("Compare Arrays do not have the same length!!!");
			System.exit(1);
		}
		
		int len = src.length;
		int changes = 0;
		float sumOld = 0;
		float sumNew = 0;
		float diff = 0;
		
		for (int i=0;i<src.length;i++){
			if (src[i] != dest[i]) {
				changes++;
				sumOld += src[i];
				sumNew += dest[i];
				diff += (dest[i]-src[i])*(dest[i]-src[i]);
			}
		}
		
		System.out.printf("\n\nProcessing %s changes\n",name);
		
		System.out.printf("   %d out of %d elements changed.\n",changes,len);
		System.out.printf("Average of Old Array: %f\n",sumOld/changes);
		System.out.printf("Average of New Array: %f\n",sumNew/changes);
		
		System.out.printf("Differential: %f\n",diff/changes);
		
	}//processArrayChanges method
	
}//Network class

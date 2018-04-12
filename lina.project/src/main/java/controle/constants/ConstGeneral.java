package controle.constants;

import view.Principal;
import view.manualviews.InternalFrame;
import view.manualviews.PainelBox;

/**
 *
 * @author Wesllen Sousa
 */
public class ConstGeneral {

    public static Principal TELA_PRINCIPAL;
    public static InternalFrame CURRENT_INTERNAL_FRAME;
    public static PainelBox CURRENT_BOX;

    /**
     * Screen Parameters
     */
    public static int OFFSET = 1;
    public static boolean NUM_REDUCTION = true;
    public static boolean ALINGMENT = true;
    public static boolean CLEAR_HIST = true;
    public static boolean HORIZONTAL_FORMAT_NOISE = true;

    public static int PP_EDIT = 2;
    public static int PP_DELETE = 3;

    public static String DATE_FORMAT = "dd-MM-yyyy HH:mm:ss";

    //Signal Processing
    public static String SP_GausianLowpass = "GaussianLowpass";
    public static String SP_ButterworthLowpass = "ButterWorthLowpass";
    public static String SP_SingleLowPass = "SimpleLowPass";
    public static String SP_FourStageLowPass = "FourStageLowpass";
    public static String SP_bandPass = "BandPass";
    public static String SP_HighPass = "HighPass";
    public static String SP_MovingAverageFilter = "MovingAverageFilter";

    //Principal Features
    public static String PF_MAGNITUDE = "Magnitude";
    public static String PF_VerticalAxis = "Vertical";
    public static String PF_HorizontalAxis = "Horizontal";
    public static String PF_ForwardAxis = "Forward";
    public static String PF_SidewardAxis = "Sideward";
    public static String PF_FastFourierTrasform = "FFT";
    public static String PF_HaarWavelet = "HaarWavelet";
    public static String PF_TiltAngle = "TiltAngle";

    //Time Features
    public static String TF_SUM = "Sum";
    public static String TF_Energy = "Energy";
    public static String TF_EnergyLog = "EnergyLog";
    public static String TF_Power = "Power";
    public static String TF_AbsoluteSum = "AbsoluteSum";
    public static String TF_MEAN = "Mean";
    public static String TF_AbsoluteMean = "AbsoluteMean";
    public static String TF_AbsoluteMeanDeviation = "AbsoluteMeanDeviation";
    public static String TF_SumSquareError = "SumSquareError";
    public static String TF_Variance = "Variance";
    public static String TF_StandardDeviation = "StandardDeviation";
    public static String TF_PearsonCoefficientVariation = "PearsonCoeficienteVariation";
    public static String TF_MIN = "MIN";
    public static String TF_MAX = "MAX";
    public static String TF_AMPLITUDE = "Amplitude";
    public static String TF_PeakAmplitude = "PeakAmplitude";
    public static String TF_EuclideanNorm = "EuclideanNorm";
    public static String TF_RootMeanSquare = "RootMeanSquare";
    public static String TF_Correlation = "Correlation";
    public static String TF_CrossCorrelation = "CrossCorrelation";
    public static String TF_AutoCorrelation = "AutoCorrelation";
    public static String TF_ZeroCrossingRatio = "ZeroCrossingRate";
    public static String TF_Quartile25 = "Quartile25";
    public static String TF_Quartile50 = "Quartile50";
    public static String TF_Quartile75 = "Quartile75";
    public static String TF_InterQuartileRange = "InterQuartileRange";
    public static String TF_AREA = "Area";
    public static String TF_AbsoluteArea = "AArea";
    public static String TF_SignalMagnitudeMean = "SignalMagnitudeMean";
    public static String TF_SignalMagnitudeArea = "SignalMagnitudeArea";
    public static String TF_AverateMagnitudeDifferenceFuction = "AverageMagnitudeDiffFunction"; //validar
    public static String TF_Skewness = "Skewness";
    public static String TF_Kurtosis = "Kurtosis";
    public static String TF_BinnedDistribution = "BinnedDistribution";

    //Frequency Features
    public static String FF_SpectralCentroid = "SpectralCentroid";
    public static String FF_SpectralEnergy = "SpectralEnergy";
    public static String FF_SpectralEnergyNormalized = "SpectralEnergyNormalized";
    public static String FF_SpectralPower = "SpectralPower";
    public static String FF_SpectralEntropy = "SpectralEntropy";
    public static String FF_CoefficientsSum = "CoefficientSum";
    public static String FF_Peaks = "Peaks";
    public static String FF_DComponents = "DComponents";
    public static String FF_DominantFrequency = "Dominant Frequency";

    //Classifiers
    public static String AL_DecisionTable = "DecisionTable";
    public static String AL_Decision_Stump = "DecisionStump";
    public static String AL_J48 = "DecisionTree";
    public static String AL_Random_Forest = "RandomForest";
    public static String AL_Random_Tree = "RandomTree";
    public static String AL_KNN = "KNN";
    public static String AL_NaiveBayes = "NaiveBayes";
    public static String AL_SVM = "SVM";

    public static String AL_Logistic = "Logistic";
    public static String AL_MultilayerPerceptron = "MultilayerPerceptron";
    public static String AL_AdaBoost = "AdaBoost";

    public static String AL_SAX_VSM = "SaxVsm";
    public static String AL_BOSS_ENSEMBLE = "BossEnsemble";
    public static String AL_BOSS_VS = "BossVS";
    public static String AL_WEASEL = "Weasel";
    public static String AL_SHOTGUN = "Shotgun";
    public static String AL_SHOTGUN_ENSEMBLE = "ShotgunEnsemble";
    public static String AL_BOSS_MD_STACK = "BossMdStack";
    public static String AL_BOSS_MD_WORDS = "BossMdWords";
    public static String AL_MUSE = "MUSE";

    //Data Fusion
    public static String DF_MAGNITUDE = "Magnitude";
    public static String DF_PCA = "PCA";
    public static String DF_KALMAN = "Kalman";

}

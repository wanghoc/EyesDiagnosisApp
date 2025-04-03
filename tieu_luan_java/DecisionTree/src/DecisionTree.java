// Source code is decompiled from a .class file using FernFlower decompiler.
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Random;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NumericToNominal;

public class DecisionTree {
    public DecisionTree() {
    }

    public static void main(String[] args) {
        try {
            String filePath = "dataset/dataset.csv";
            Instances data = loadData(filePath);
            data = preprocessData(data);
            checkLabelDistribution(data, "Dataset ban đầu:");
            Instances[] split = splitData(data, 0.8, 1);
            Instances trainData = split[0];
            Instances testData = split[1];
            checkLabelDistribution(trainData, "Tập huấn luyện:");
            checkLabelDistribution(testData, "Tập kiểm thử:");
            saveFeatureStructure(data, "models/features.arff");
            saveClassInfo(data, "models/class_info.txt");
            J48 model = trainModel(trainData);
            evaluateModel(model, trainData, testData);
            saveModel(model, "models/decision_tree_model.j48");
        } catch (Exception var7) {
            var7.printStackTrace();
        }

    }

    public static Instances loadData(String filePath) throws Exception {
        ConverterUtils.DataSource source = new ConverterUtils.DataSource(filePath);
        Instances data = source.getDataSet();
        data.setClassIndex(0);
        return data;
    }

    public static Instances preprocessData(Instances data) throws Exception {
        if (data.classAttribute().isNumeric()) {
            NumericToNominal filter = new NumericToNominal();
            filter.setAttributeIndices("first");
            filter.setInputFormat(data);
            data = Filter.useFilter(data, filter);
        }

        return data;
    }

    public static void checkLabelDistribution(Instances data, String tag) {
        System.out.println("\nTỷ lệ phân bố các lớp của " + tag);
        System.out.println(data.attributeStats(data.classIndex()));
    }

    public static Instances[] splitData(Instances data, double trainRatio, int seed) {
        int folds = (int)Math.round(1.0 / (1.0 - trainRatio));
        data.randomize(new Random((long)seed));
        if (data.classAttribute().isNominal()) {
            data.stratify(folds);
        }

        Instances trainData = data.trainCV(folds, 0, new Random((long)seed));
        Instances testData = data.testCV(folds, 0);
        return new Instances[]{trainData, testData};
    }

    public static J48 trainModel(Instances trainData) throws Exception {
        System.out.println("\nĐang huấn luyện mô hình cây quyết định (J48)...");
        J48 tree = new J48();
        tree.setOptions(new String[]{"-C", "0.3", "-M", "5"});
        tree.buildClassifier(trainData);
        System.out.println("\nCấu trúc cây quyết định đã học:");
        System.out.println(tree);
        return tree;
    }

    public static void evaluateModel(J48 model, Instances trainData, Instances testData) throws Exception {
        Evaluation eval = new Evaluation(trainData);
        eval.evaluateModel(model, testData, new Object[0]);
        System.out.println("\nĐánh giá kết quả trên tập kiểm thử:");
        System.out.println(eval.toSummaryString("\nTổng kết kết quả:\n", false));
        System.out.println(eval.toClassDetailsString("\nChi tiết kết quả từng lớp:\n"));
        System.out.println(eval.toMatrixString("\nMa trận nhầm lẫn:\n"));
        Evaluation evalCrossValidation = new Evaluation(trainData);
        evalCrossValidation.crossValidateModel(model, trainData, 10, new Random(1L));
        System.out.println(evalCrossValidation.toSummaryString("\nKết quả Cross-Validation (10-fold):\n", false));
    }

    public static void saveModel(J48 model, String modelPath) throws Exception {
        File modelDir = new File("models");
        if (!modelDir.exists()) {
            modelDir.mkdirs();
        }

        SerializationHelper.write(modelPath, model);
        System.out.println("\nĐã lưu mô hình vào đường dẫn: " + modelPath);
    }

    public static void saveClassInfo(Instances data, String filePath) throws Exception {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        Attribute classAttribute = data.classAttribute();
        writer.write("Class Attribute: " + classAttribute.name() + "\n");
        writer.write("Values:\n");

        for(int i = 0; i < classAttribute.numValues(); ++i) {
            String var10001 = classAttribute.value(i);
            writer.write(" - " + var10001 + "\n");
        }

        writer.close();
    }

    public static void saveFeatureStructure(Instances data, String filePath) throws Exception {
        Instances structureOnly = new Instances(data, 0);
        ArffSaver saver = new ArffSaver();
        saver.setInstances(structureOnly);
        saver.setFile(new File(filePath));
        saver.writeBatch();
    }
}

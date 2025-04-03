package com.diagnosis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Random;

import weka.classifiers.Evaluation;
import weka.classifiers.trees.RandomForest;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NumericToNominal;

public class RandomForestClassifier {
    public RandomForestClassifier() {
    }

    public static void main(String[] args) {
        try {
            // Sửa đường dẫn tương đối đến file dataset
            String filePath = "SharedLibrary/dataset/dataset.csv";

            // Tạo thư mục models nếu chưa tồn tại
            File modelDir = new File("SharedLibrary/models");
            if (!modelDir.exists()) {
                modelDir.mkdirs();
            }

            Instances data = loadData(filePath);
            data = preprocessData(data);
            checkLabelDistribution(data, "Dataset ban đầu:");
            Instances[] split = splitData(data, 0.8, 1);
            Instances trainData = split[0];
            Instances testData = split[1];
            checkLabelDistribution(trainData, "Tập huấn luyện:");
            checkLabelDistribution(testData, "Tập kiểm thử:");
            // Sửa đường dẫn lưu các file output
            saveFeatureStructure(data, "SharedLibrary/models/features.arff");
            saveClassInfo(data, "SharedLibrary/models/class_info.txt");
            RandomForest model = trainModel(trainData);
            evaluateModel(model, trainData, testData);
            saveModel(model, "SharedLibrary/models/random_forest_model.rf");
        } catch (Exception e) {
            e.printStackTrace();
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
        int folds = (int) Math.round(1.0 / (1.0 - trainRatio));
        data.randomize(new Random((long) seed));
        if (data.classAttribute().isNominal()) {
            data.stratify(folds);
        }
        Instances trainData = data.trainCV(folds, 0, new Random((long) seed));
        Instances testData = data.testCV(folds, 0);
        return new Instances[] { trainData, testData };
    }

    public static RandomForest trainModel(Instances trainData) throws Exception {
        System.out.println("\nĐang huấn luyện mô hình Random Forest...");
        RandomForest rf = new RandomForest();
        // Thiết lập tham số bằng setOptions (phù hợp với Weka 3.9.6)
        rf.setOptions(weka.core.Utils.splitOptions("-P 100 -I 100 -num-slots 1 -K 0 -M 1.0 -V 0.001 -S 1"));
        /*
         * Giải thích tham số:
         * -P 100: Tỷ lệ kích thước túi (bag size) là 100% (mặc định)
         * -I 100: Số lượng cây (numIterations = 100)
         * -num-slots 1: Số luồng thực thi (1 = không song song)
         * -K 0: Số đặc trưng ngẫu nhiên (0 = log2(numFeatures) + 1)
         * -M 1.0: Số lượng mẫu tối thiểu tại mỗi lá
         * -V 0.001: Variance tối thiểu để chia nhánh
         * -S 1: Seed cho random
         */
        rf.buildClassifier(trainData);
        System.out.println("\nThông tin mô hình Random Forest:");
        System.out.println(rf);
        return rf;
    }

    public static void evaluateModel(RandomForest model, Instances trainData, Instances testData) throws Exception {
        Evaluation eval = new Evaluation(trainData);
        eval.evaluateModel(model, testData);
        System.out.println("\nĐánh giá kết quả trên tập kiểm thử:");
        System.out.println(eval.toSummaryString("\nTổng kết kết quả:\n", false));
        System.out.println(eval.toClassDetailsString("\nChi tiết kết quả từng lớp:\n"));
        System.out.println(eval.toMatrixString("\nMa trận nhầm lẫn:\n"));
        Evaluation evalCrossValidation = new Evaluation(trainData);
        evalCrossValidation.crossValidateModel(model, trainData, 10, new Random(1L), new Object[] {});
        System.out.println(evalCrossValidation.toSummaryString("\nKết quả Cross-Validation (10-fold):\n", false));
    }

    public static void saveModel(RandomForest model, String modelPath) throws Exception {
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
        for (int i = 0; i < classAttribute.numValues(); ++i) {
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
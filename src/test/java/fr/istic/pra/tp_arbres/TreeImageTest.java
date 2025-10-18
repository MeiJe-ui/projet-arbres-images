package fr.istic.pra.tp_arbres;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.abort;

import org.junit.jupiter.api.function.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.BiPredicate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import fr.istic.pra.util.BinaryTree;
import fr.istic.pra.tp_arbres.interfaces.Image;
import fr.istic.pra.tp_arbres.tree_image.Node;
import fr.istic.pra.tp_arbres.tree_image.TreeImage;

/**
 * Unit tests for TreeImage class.
 * @author Vincent Drevelle <vincent.drevelle@univ-rennes.fr>
 * @version 16 octobre 2025
 *

 */
public class TreeImageTest {
    static final Map<String, UnaryOperator<TreeImage>> imageTransformations = new HashMap<>();
    static {
        imageTransformations.put("copy", TreeImage::copy);
        imageTransformations.put("rotated180", TreeImage::rotated180);
        imageTransformations.put("inverted", TreeImage::inverted);

        imageTransformations.put("flippedHorizontal", TreeImage::flippedHorizontal);
        imageTransformations.put("rotatedClockwise90", TreeImage::rotatedClockwise90);


    }
    static final Map<String, BiConsumer<TreeImage, TreeImage>> imageCombinations = new HashMap<>();
    static {
        imageCombinations.put("affect", TreeImage::affect);

        imageCombinations.put("intersection", TreeImage::intersection);


    }
    static final Map<String, Predicate<TreeImage>> imagePredicates = new HashMap<>();
    static {

        imagePredicates.put("testDiagonal", TreeImage::testDiagonal);
    }
    static final Map<String, BiPredicate<TreeImage, TreeImage>> imageBiPredicates = new HashMap<>();
    static {
        imageBiPredicates.put("equals", TreeImage::equals);

    }
    static final Map<String, Consumer<TreeImage>> imageConsumers = new HashMap<>();
    static {
        imageConsumers.put("fill(0)", image -> image.fill(0));
        imageConsumers.put("fill(1)", image -> image.fill(1));
    }
    

    private static Stream<Arguments> streamImageTransformations() {
        return imageTransformations.entrySet().stream()
            .map(entry -> Arguments.of(entry.getKey(), entry.getValue()));
    }
    private static Stream<Arguments> streamImageCombinations() {
        return imageCombinations.entrySet().stream()
            .map(entry -> Arguments.of(entry.getKey(), entry.getValue()));
    }
    private static Stream<Arguments> streamImagePredicates() {
        return imagePredicates.entrySet().stream()
            .map(entry -> Arguments.of(entry.getKey(), entry.getValue()));
    }
    private static Stream<Arguments> streamImageBiPredicates() {
        return imageBiPredicates.entrySet().stream()
            .map(entry -> Arguments.of(entry.getKey(), entry.getValue()));
    }
    private static Stream<Arguments> streamImageConsumers() {
        return imageConsumers.entrySet().stream()
            .map(entry -> Arguments.of(entry.getKey(), entry.getValue()));
    }
    private static Stream<Arguments> streamAllMethods() {
        return Stream.of(
            streamImageTransformations(),
            streamImageCombinations(),
            streamImagePredicates(),
            streamImageBiPredicates(),
            streamImageConsumers()
        ).flatMap(stream -> stream);
    }

    /**
     * Load and save an image. Should be identical.
     */
    @Test
    public void testLoadAndSaveImage() throws IOException {
        // Load an image
        TreeImage image = new TreeImage();
        assertTrue(image.load("images/image.tree"), "Loading image.tree failed");

        // Save the image
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        assertTrue(image.save(os), "Saving image to output stream failed");

        // Load the image file in a byte array
        FileInputStream is = new FileInputStream("images/image.tree");
        assertArrayEquals(is.readAllBytes(), os.toByteArray(), "Saved image does not match original file");
        is.close();
    }

    /**
     * Check rotated180
     */
    @Test
    public void testRotated180() throws IOException {
        // Load an image
        TreeImage image = new TreeImage();
        assertTrue(image.load("images/cartoon.tree"), "Loading images/cartoon.tree failed");
        TreeImage rotated = image.rotated180();
        assertNotNull(rotated, "rotated180 returned null");
        assertNotSame(image, rotated, "rotated180 returned the same object");
        FileInputStream groundTruthStream = new FileInputStream("images/cartoon180.tree");
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        assertTrue(rotated.save(os), "Saving rotated image to output stream failed");
        assertArrayEquals(groundTruthStream.readAllBytes(), os.toByteArray(), "rotated180 result does not match ground truth");
        groundTruthStream.close();
    }

    /**
     * Check that operators do not modify the original image.
     */
    @ParameterizedTest
    @MethodSource("streamImageTransformations")
    @DisplayName("Les opérations unaires TreeImage ne modifient pas l'image originale")
    public void testImageOperatorsDoNotModifyOriginal(String opName, UnaryOperator<TreeImage> op) throws IOException {
        FileInputStream is = new FileInputStream("images/image.tree");
        byte[] origData = is.readAllBytes();
        is.close();
        // Load the image
        TreeImage image = new TreeImage();
        assertTrue(image.load(new ByteArrayInputStream(origData)), "Loading image.tree failed");

        // Apply the operation
        TreeImage result = null;
        try {
            result = op.apply(image);
        } catch (UnsupportedOperationException e) {
            abort(String.format("%s: Operation not implemented", opName)); // Operation not implemented, skip
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        assertTrue(image.save(os), "Saving image to output stream failed");
        // Check that the original image is unchanged
        assertArrayEquals(origData, os.toByteArray(), opName + " modified the original image");

        // Check that the result is not null and is a different object
        assertNotNull(result, opName + " returned null");
        assertNotSame(image, result, opName + " returned the same object as the original image");

        // Check that the result is an independent copy
        result.getTree().setRootValue(Node.valueOf(0)); // Modify the result tree
        os.reset();
        assertTrue(image.save(os), "Saving image to output stream failed");
        assertArrayEquals(origData, os.toByteArray(), opName + " result is not independent from the original image");
        result.getTree().setRootValue(Node.valueOf(1)); // Modify the result tree
        os.reset();
        assertTrue(image.save(os), "Saving image to output stream failed");
        assertArrayEquals(origData, os.toByteArray(), opName + " result is not independent from the original image");
    }

    /**
     * Check that binary operators do not modify the original images.
     */
    @DisplayName("Les opérations binaires TreeImage ne modifient pas les images originales")
    @ParameterizedTest
    @MethodSource("streamImageCombinations")
    public void testImageBinaryOperatorsDoNotModifyOriginals(String opName, BiConsumer<TreeImage, TreeImage> op) throws IOException {
        FileInputStream is = new FileInputStream("images/image.tree");
        byte[] origData1 = is.readAllBytes();
        is.close();
        is = new FileInputStream("images/a2.tree");
        byte[] origData2 = is.readAllBytes();
        is.close();

        // Load the images
        TreeImage image1 = new TreeImage();
        assertTrue(image1.load(new ByteArrayInputStream(origData1)), "Loading image.tree failed");
        TreeImage image2 = new TreeImage();
        assertTrue(image2.load(new ByteArrayInputStream(origData2)), "Loading a2.tree failed");

        // Apply the operation
        try {
            op.accept(image1, image2);
        } catch (UnsupportedOperationException e) {
            abort(String.format("%s: Operation not implemented", opName));
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        // Check that the original image is unchanged
        assertTrue(image2.save(os), "Saving image to output stream failed");
        assertArrayEquals(origData2, os.toByteArray(), opName + " modified the original image2");

        // Check that the result is an independent copy
        image1.getTree().setRootValue(Node.valueOf(0)); // Modify the result tree
        os.reset();
        assertTrue(image2.save(os), "Saving image to output stream failed");
        assertArrayEquals(origData2, os.toByteArray(), opName + " result is not independent from the original image1");
        image1.getTree().setRootValue(Node.valueOf(1)); // Modify the result tree
        os.reset();
        assertTrue(image2.save(os), "Saving image to output stream failed");
        assertArrayEquals(origData2, os.toByteArray(), opName + " result is not independent from the original image2");
    }

    /**
     * Check that TreeImage operations return a TreeImage based on the same tree implementation.
     */
    @DisplayName("Les opérations TreeImage retournent le même type d'image")
    @ParameterizedTest
    @MethodSource("streamImageTransformations")
    public void treeImageOperationsReturnSameType(String opName, UnaryOperator<TreeImage> op) {
        class CustomTree extends fr.istic.pra.util.BinaryTreeImpl<Node> {
            // You can override methods here if needed
        };
        class TestTreeImage extends TreeImage {
            @Override
            protected TestTreeImage createTreeImage() {
                return new TestTreeImage();
            }
        };

        // Skip if not implemented
        try {
            op.apply(new TreeImage());
        } catch (UnsupportedOperationException e) {
            abort(String.format("%s: Operation not implemented", opName));
        }

        TestTreeImage testImage = new TestTreeImage();
        assertEquals(testImage.getClass(), op.apply(testImage).getClass(), opName + " did not return a TreeImage of the same type: utilisez createTreeImage() au lieu de new TreeImage()");

        TreeImage image = new TreeImage();
        assertEquals(image.getTree().getClass(), op.apply(image).getTree().getClass(), opName + " did not return a TreeImage of the same type: utilisez createTreeImage() et createNewTree() au lieu de new TreeImage() et new BinaryTreeImplXXX()");

        image = new TreeImage(fr.istic.pra.util.BinaryTreeImpl::new);
        assertEquals(image.getTree().getClass(), op.apply(image).getTree().getClass(), opName + " did not return a TreeImage of the same type (BinaryTreeImpl class): utilisez createTreeImage() et createNewTree() au lieu de new TreeImage() et new BinaryTreeImplXXX()");

        TreeImage customImage = new TreeImage(CustomTree::new);
        assertEquals(customImage.getTree().getClass(), op.apply(customImage).getTree().getClass(), opName + " did not return a TreeImage of the same type (CustomTree class): utilisez createTreeImage() et createNewTree() au lieu de new TreeImage() et new BinaryTreeImplXXX()");
    }

    /**
     * Check that all TreeImage operations do not use isPixelOn or drawPixel methods.
     * This is to ensure that operations are implemented using tree manipulations only.
     * (This test is a bit artificial since we cannot easily check method calls.)
     */
    @DisplayName("Ne pas utiliser isPixelOn ou drawPixel dans les opérations TreeImage")
    @ParameterizedTest
    @MethodSource("streamAllMethods")
    @SuppressWarnings("unchecked")
    public void treeImageOperationsDoNotUsePixelMethods(String opName, Object op) {
        // Custom exceptions to detect method calls
        class IsPixelOnCalledException extends RuntimeException {}
        class DrawPixelCalledException extends RuntimeException {}
        // A TreeImage subclass that throws an exception if isPixelOn or drawPixel are called
        class TestTreeImage extends TreeImage {
            @Override
            public boolean isPixelOn(int x, int y) {
                throw new IsPixelOnCalledException();
            }
            @Override
            public void drawPixel(int x, int y, int color) {
                throw new DrawPixelCalledException();
            }
            @Override
            protected TestTreeImage createTreeImage() {
                return new TestTreeImage();
            }
        };

        TestTreeImage image = new TestTreeImage();
        TestTreeImage image2 = new TestTreeImage();
        try {
            if (op instanceof UnaryOperator uop) {
                uop.apply(image);
            } else if (op instanceof Consumer cop) {
                cop.accept(image);
            }else if (op instanceof BiConsumer biop) {
                biop.accept(image, image2);
            } else if (op instanceof Predicate pop) {
                pop.test(image);
            } else if (op instanceof BiPredicate bipop) {
                bipop.test(image, image2);
            }
        } catch (IsPixelOnCalledException | DrawPixelCalledException e) {
            fail(String.format("%s operation called pixel method: %s", opName, e.getClass().getSimpleName()));
        } catch (Exception e) {
            // Ignore other exceptions
        }
    }

    /**
     * Check that combinations (binary operations) with the caller TreeImage are valid.
     */
    @DisplayName("Appels des opérations sur le même objet image (this == autre image)")
    @ParameterizedTest
    @MethodSource("streamImageCombinations")
    public void imageCombinationsWithCaller(String opName, BiConsumer<TreeImage, TreeImage> biop) throws IOException {
        TreeImage image = new TreeImage();
        try {
            biop.accept(image, image);
        } catch (Exception e) {
            fail(String.format("img.%s(img) à échoué : %s", opName, e.getMessage()));
        }
    }


    /////////////////////////////////////////////////////////////////////////////////////
    ////// Tests convertis depuis TestImage.java de la version précédente du TP ////////
    /////////////////////////////////////////////////////////////////////////////////////
    // --- Tests adaptés depuis TestImage.java (ancienne version) ---

    private static TreeImage loadTreeImage(String fileName) throws IOException {
        TreeImage image = new TreeImage();
        assertTrue(image.load("images/" + fileName), "Loading images/" + fileName + " failed");
        return image;
    }

    private static boolean compareTreeImages(TreeImage img1, TreeImage img2) throws IOException {
        ByteArrayOutputStream os1 = new ByteArrayOutputStream();
        ByteArrayOutputStream os2 = new ByteArrayOutputStream();
        assertTrue(img1.save(os1), "Saving img1 failed");
        assertTrue(img2.save(os2), "Saving img2 failed");
        return java.util.Arrays.equals(os1.toByteArray(), os2.toByteArray());
    }

    @ParameterizedTest
    @CsvSource({
        "a1.tree, 0, 128, true",
        "a1.tree, 128, 0, false",
        "a1.tree, 192, 128, false",
        "a1.tree, 128, 192, false",
        "a1.tree, 255, 192, false",
        "a1.tree, 192, 255, false",
        "a2.tree, 0, 128, false",
        "a2.tree, 128, 0, false",
        "a2.tree, 192, 128, false",
        "a2.tree, 128, 192, true",
        "a2.tree, 255, 192, false",
        "a2.tree, 192, 255, false",
        "a2.tree, 32, 128, true",
        "a2.tree, 213, 95, false",
        "a2.tree, 97, 97, true",
        "a2.tree, 5, 249, false",
        "a2.tree, 249, 5, false",
        "a2.tree, 5, 5, false",
    })
    @DisplayName("test isPixelOn avec divers fichiers")
    public void testIsPixelOnFichiers(String fileName, int x, int y, boolean expected) throws IOException {
        TreeImage image = loadTreeImage(fileName);
        assertEquals(expected, image.isPixelOn(x, y), String.format("pixel %s %d %d", fileName, x, y));
    }


    @ParameterizedTest
    @CsvSource({
        "a1.tree, a2.tree, test-i12.tree",
        "a2.tree, a6.tree, test-i26.tree",
    })
    @DisplayName("test intersection avec divers fichiers")
    public void testIntersectionFichiers(String file1, String file2, String expectedFile) throws IOException {
        TreeImage image1 = loadTreeImage(file1);
        TreeImage image2 = loadTreeImage(file2);
        TreeImage expected = loadTreeImage(expectedFile);
        TreeImage result = image1.copy();
        result.intersection(image2);
        assertTrue(compareTreeImages(result, expected), "intersection " + file1 + " et " + file2);
    }

    @Test
    public void testAffectA1() throws IOException {
        TreeImage image1 = loadTreeImage("a1.tree");
        TreeImage image2 = new TreeImage();
        image2.affect(image1);
        assertTrue(compareTreeImages(image1, image2), "affect a1");
    }

    @Test
    public void testAffectA1ToA2() throws IOException {
        TreeImage image1 = loadTreeImage("a1.tree");
        TreeImage image2 = loadTreeImage("a2.tree");
        image2.affect(image1);
        assertTrue(compareTreeImages(image1, image2), "affect a1 to a2");
    }

    @Test
    public void testRotated180A1() throws IOException {
        TreeImage image1 = loadTreeImage("a1.tree");
        TreeImage expected = loadTreeImage("test-r1.tree");
        TreeImage result = image1.rotated180();
        assertTrue(compareTreeImages(result, expected), "rotation a1");
    }

    @Test
    public void testVideoInverseA2() throws IOException {
        TreeImage image1 = loadTreeImage("a2.tree");
        TreeImage expected = loadTreeImage("test-i2.tree");
        TreeImage result = image1.inverted();
        assertTrue(compareTreeImages(result, expected), "videoInverse a2");
    }

    @Test
    public void testMirrorHCartoon() throws IOException {
        TreeImage image1 = loadTreeImage("cartoon.tree");
        TreeImage expected = loadTreeImage("cartoonh.tree");
        TreeImage result = image1.flippedHorizontal();
        assertTrue(compareTreeImages(result, expected), "mirrorH cartoon");
    }



    @ParameterizedTest
    @CsvSource({
        "a3.tree, a3.tree",
        "cartoon90.tree, cartoon.tree",
    })
    @DisplayName("testRotateClockwise90 avec divers fichiers")
    public void testRotateClockwise90(String inputFile, String expectedFile) throws IOException {
        TreeImage image1 = loadTreeImage(inputFile);
        TreeImage expected = loadTreeImage(expectedFile);
        TreeImage result = image1.rotatedClockwise90();
        assertTrue(compareTreeImages(result, expected), "rotatedClockwise90 " + inputFile);
    }



    @ParameterizedTest
    @CsvSource({
        "d2.tree, true",
        "d3.tree, false",
        "a5.tree, false",
        "d4.tree, true",
        "d7.tree, false",
        "d8.tree, false",
        "d9.tree, false",
    })
    @DisplayName("testDiagonal avec divers fichiers")
    public void testDiagonalVarious(String fileName, boolean expected) throws IOException {
        TreeImage image = loadTreeImage(fileName);
        assertEquals(expected, image.testDiagonal(), "diagonale " + fileName);
    }

    
}

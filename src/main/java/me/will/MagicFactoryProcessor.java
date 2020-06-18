package me.will;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@AutoService(Processor.class)
public class MagicFactoryProcessor extends AbstractProcessor {

	@Override
	public Set<String> getSupportedAnnotationTypes() {
		Set<String> set = new HashSet<>();
		set.add(Magic.class.getName());
		return set;
	}

	@Override
	public SourceVersion getSupportedSourceVersion() {
		return SourceVersion.latestSupported();
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Magic.class);
		for (Element element : elements) {
			Name simpleName = element.getSimpleName();
			if (element.getKind() != ElementKind.INTERFACE) {
				processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Magic annotation can't be used on" + simpleName);
			} else {
				processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Processing " + simpleName);
			}

			MethodSpec pullOut = MethodSpec.methodBuilder("pullOut")
					.addModifiers(Modifier.PUBLIC)
					.returns(String.class)
					.addStatement("return $S", "Rabbit!")
					.build();

			TypeSpec magicFactory = TypeSpec.classBuilder("MagicFactory")
					.addModifiers(Modifier.PUBLIC)
					.addMethod(pullOut)
					.build();

			TypeElement typeElement = (TypeElement) element;
			ClassName className = ClassName.get(typeElement);

			Filer filer = processingEnv.getFiler();
			try {
				JavaFile.builder(className.packageName(), magicFactory)
						.build()
						.writeTo(filer);
			} catch (IOException e) {
				processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "FATAL ERROR : " + e);
			}
		}
		return true;
	}

}

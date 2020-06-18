package me.will;

import com.google.auto.service.AutoService;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
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
		}
		return true;
	}

}

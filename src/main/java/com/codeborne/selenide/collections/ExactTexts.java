package com.codeborne.selenide.collections;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.ex.ElementNotFound;
import com.codeborne.selenide.ex.TextsMismatch;
import com.codeborne.selenide.impl.Html;
import com.codeborne.selenide.impl.WebElementsCollection;
import org.openqa.selenium.WebElement;

import java.util.Arrays;
import java.util.List;

public class ExactTexts extends CollectionCondition {
  protected final String[] expectedTexts;

  public ExactTexts(final String... expectedTexts) {
    if (expectedTexts.length == 0) {
      throw new IllegalArgumentException("Array of expected texts is empty");
    }
    this.expectedTexts = expectedTexts;
  }

  public ExactTexts(final List<String> expectedTexts) {
    if (expectedTexts.size() == 0) {
      throw new IllegalArgumentException("The list of expected texts is empty");
    }
    this.expectedTexts = expectedTexts.toArray(new String[expectedTexts.size()]);
  }

  @Override
  public boolean apply(List<WebElement> elements) {
    if (elements.size() != expectedTexts.length) {
      return false;
    }

    for (int i = 0; i < expectedTexts.length; i++) {
      WebElement element = elements.get(i);
      String expectedText = expectedTexts[i];
      if (!Html.text.equals(element.getText(), expectedText)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public void fail(WebElementsCollection collection, List<WebElement> elements, Exception lastError, long timeoutMs) {
    if (elements == null || elements.isEmpty()) {
      ElementNotFound elementNotFound = new ElementNotFound(collection, expectedTexts, lastError);
      elementNotFound.timeoutMs = timeoutMs;
      throw elementNotFound;
    } else {
      throw new TextsMismatch(collection, ElementsCollection.getTexts(elements), expectedTexts, timeoutMs);
    }
  }

  @Override
  public String toString() {
    return "Exact texts " + Arrays.toString(expectedTexts);
  }
}

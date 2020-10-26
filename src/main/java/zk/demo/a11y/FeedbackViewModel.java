package zk.demo.a11y;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.zk.ui.util.Clients;

public class FeedbackViewModel {

	private FeedbackForm formContent;

	private Validator pizzaCountValidator;
	private Validator commentValidator;

	@Init
	public void init() {
		formContent = new FeedbackForm();

		pizzaCountValidator = new AbstractValidator() {
			@Override
			public void validate(ValidationContext ctx) {
				Object raw = ctx.getProperty().getValue();
				int value = raw != null ? (int) raw : 0;
				if (value < 1) {
					addInvalidMessage(ctx, "Pizza count must be more than 0: " + value);
				}
				if (value > 10) {
					addInvalidMessage(ctx, "Maximum pizza per order is 10: " + value);
				}
			}
		};

		commentValidator = new AbstractValidator() {
			@Override
			public void validate(ValidationContext ctx) {
				String value = (String) ctx.getProperty().getValue();
				if (null == value || "".equals(value)) {
					addInvalidMessage(ctx, "Comment cannot be empty");
				}
			}
		};
	}

	@Command
	public void save() {
		Clients.showNotification("Thank you for your feedback.");
		formContent = new FeedbackForm();
		BindUtils.postNotifyChange(this, "formContent");
	}

	public Validator getPizzaCountValidator() {
		return pizzaCountValidator;
	}

	public Validator getCommentValidator() {
		return commentValidator;
	}

	public FeedbackForm getFormContent() {
		return formContent;
	}
}

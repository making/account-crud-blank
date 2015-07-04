package am.ik.archetype.app.account;

import am.ik.archetype.domain.model.Account;
import am.ik.archetype.domain.model.AccountStatus;
import am.ik.archetype.domain.model.Role;
import am.ik.archetype.domain.service.account.AccountService;
import am.ik.archetype.domain.validation.groups.CrudMode;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.groups.Default;


@Controller
@RequestMapping("account")
public class AccountController {
    @Autowired
    AccountService accountService;
    @Autowired
    LockedValidator lockedValidator;

    @ModelAttribute
    AccountForm accountForm() {
        return new AccountForm();
    }

    @ModelAttribute
    Role[] roles() {
        return Role.values();
    }

    @ModelAttribute
    AccountStatus[] accountStatusList() {
        return AccountStatus.values();
    }

    @RequestMapping
    String list(Model model, @PageableDefault Pageable pageable) {
        Page<Account> accountPage = accountService.findAll(pageable);
        model.addAttribute("page", accountPage);
        return "account/list";
    }

    @RequestMapping(value = "create", method = RequestMethod.GET, params = "form")
    String createForm() {
        return "account/createForm";
    }

    @RequestMapping(value = "create", method = RequestMethod.POST, params = "redo")
    String createRedo(AccountForm form) {
        return "account/createForm";
    }

    @RequestMapping(value = "create", method = RequestMethod.POST, params = "confirm")
    String createConfirm(@Validated({CrudMode.Create.class, Default.class}) AccountForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "account/createForm";
        }
        return "account/createConfirm";
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    String create(@Validated({CrudMode.Create.class, Default.class}) AccountForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "account/createForm";
        }
        Account account = new Account();
        BeanUtils.copyProperties(form, account);
        account.setAccountStatus(AccountStatus.INIT);
        accountService.create(account, form.getPassword());
        return "redirect:/account";
    }

    @RequestMapping(value = "update", method = RequestMethod.GET, params = "form")
    String updateForm(@RequestParam Long accountId, AccountForm form, Model model) {
        Account account = accountService.findOne(accountId);
        BeanUtils.copyProperties(account, form);
        form.setLocked(accountService.isLocked(account));
        return "account/updateForm";
    }

    @RequestMapping(value = "update", method = RequestMethod.POST, params = "redo")
    String updateRedo(AccountForm form) {
        return "account/updateForm";
    }

    @RequestMapping(value = "update", method = RequestMethod.POST, params = "confirm")
    String updateConfirm(@Validated({CrudMode.Update.class, Default.class}) AccountForm form, BindingResult bindingResult) {
        lockedValidator.validate(form, bindingResult);
        if (bindingResult.hasErrors()) {
            return "account/updateForm";
        }
        return "account/updateConfirm";
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    String update(@Validated({CrudMode.Update.class, Default.class}) AccountForm form, BindingResult bindingResult) {
        lockedValidator.validate(form, bindingResult);
        if (bindingResult.hasErrors()) {
            return "account/createForm";
        }
        Account account = accountService.findOne(form.getAccountId());
        BeanUtils.copyProperties(form, account);
        boolean unlock = !form.isLocked();
        if (StringUtils.isEmpty(form.getPassword().getValue())) {
            accountService.updateWithoutPassword(account, unlock);
        } else {
            accountService.updateWithNewPassword(account, form.getPassword(), unlock);
        }
        return "redirect:/account";
    }

    @RequestMapping(value = "delete", method = RequestMethod.GET, params = "confirm")
    String deleteConfirm(@RequestParam Long accountId, Model model) {
        Account account = accountService.findOne(accountId);
        model.addAttribute("account", account);
        return "account/deleteConfirm";
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    String delete(@RequestParam Long accountId) {
        accountService.delete(accountId);
        return "redirect:/account";
    }
}
